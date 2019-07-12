package com.neo.sk.Example.device

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.neo.sk.Example.device.DeviceGroupQuery.CollectionTimeout

import scala.concurrent.duration.FiniteDuration

/**
  * created by byf on 2019.7.12
  * 代表单个查询的actor，并代表组actor执行完成查询所需的任务
  * 调度查询超时
  * */
object DeviceGroupQuery{

  case object CollectionTimeout

  def props(actorToDeviceId:Map[ActorRef,String],requestId:Long,requester:ActorRef,timeout:FiniteDuration):Props =
    Props(new DeviceGroupQuery(actorToDeviceId,requestId,requester,timeout))

}
class DeviceGroupQuery (
                       actorToDeviceId:Map[ActorRef,String],
                       requestId:Long,
                       requester:ActorRef,
                       timeout:FiniteDuration
                       )extends Actor with ActorLogging{
  import DeviceGroup._
  import context.dispatcher

  private val queryTimeoutTimer = context.system.scheduler.scheduleOnce(timeout,self,CollectionTimeout)


  override def preStart(): Unit = {
    actorToDeviceId.keysIterator.foreach{deviceActor =>
      context.watch(deviceActor)
      deviceActor ! Device.ReadTemperature(0)
    }
  }

  override def postStop(): Unit = {
    queryTimeoutTimer.cancel()
  }

  override def receive: Receive = waitingForReplies(Map.empty,actorToDeviceId.keySet)

  def waitingForReplies(repliesSoFar:Map[String,DeviceGroup.TemperatureReading],
                        stillWaiting:Set[ActorRef]):Receive = {
    case Device.RespondTemperature(0,valueOption) =>
      val deviceActor = sender()
      val reading = valueOption match{
        case Some(value) => DeviceGroup.Temperature(value)
        case None => DeviceGroup.TemperatureNotAvailable
      }
      receivedRespond(deviceActor,reading,stillWaiting,repliesSoFar)

    case Terminated(deviceActor) =>
      receivedRespond(deviceActor,DeviceGroup.DeviceNotAvailable,stillWaiting,repliesSoFar)

    case CollectionTimeout =>
      val timedOutReplies = stillWaiting.map{deviceActor =>
        val deviceId = actorToDeviceId(deviceActor)
        deviceId -> DeviceGroup.DeviceTimeOut
      }
      requester ! DeviceGroup.RespondAllTemperatures(requestId,repliesSoFar ++ timedOutReplies)
      context.stop(self)

  }

  def receivedRespond(
                     deviceActor:ActorRef,
                     reading:DeviceGroup.TemperatureReading,
                     stillWaiting:Set[ActorRef],
                     repliesSoFar:Map[String,DeviceGroup.TemperatureReading]
                     ):Unit = {
    context.unwatch(deviceActor)
    val deviceId = actorToDeviceId(deviceActor)
    val newStillWaiting = stillWaiting - deviceActor

    val newRepliesSoFar = repliesSoFar + (deviceId -> reading)
    if(newStillWaiting.isEmpty){
      requester ! DeviceGroup.RespondAllTemperatures(requestId,newRepliesSoFar)
      context.stop(self)
    }else{
      context.become(waitingForReplies(newRepliesSoFar,newStillWaiting))
    }
  }


}
