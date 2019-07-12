package com.neo.sk.Example.device

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

/**
  * created by byf on 2019.7.12
  * */
object DeviceGroup{
  def props(groupId:String):Props = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId:Long)
  final case class ReplyDeviceList(requestId:Long,ids:Set[String])

  //请求一个设备组下的所有设备的温度情况
  final case class RequestAllTemperatures(requestId:Long)
  final case class RespondAllTemperatures(requestId:Long,temperatures:Map[String,TemperatureReading])

  sealed trait TemperatureReading
  final case class Temperature(value:Double) extends TemperatureReading//正常返回
  case object TemperatureNotAvailable extends TemperatureReading//设备已经启动，温度数据不可用
  case object DeviceNotAvailable extends TemperatureReading//设备不可用
  case object DeviceTimeOut extends TemperatureReading//请求超时

}

class DeviceGroup(groupId:String) extends Actor with ActorLogging{
  import com.neo.sk.Example.device.DeviceGroup._
  import com.neo.sk.Example.device.DeviceManager._

  var deviceIdToActor = Map.empty[String,ActorRef]
  var actorToDeviceId = Map.empty[ActorRef,String]

  override def preStart(): Unit = log.info("DeviceGroup {} started",groupId)

  override def postStop(): Unit = log.info("DeviceGroup {} stopped",groupId)

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(`groupId`,_) =>
      //因为域中含有groupId变量，所以不加``符号的话，会出现阴影
      deviceIdToActor.get(trackMsg.deviceId) match{
        case Some(deviceActor) =>
          deviceActor.forward(trackMsg)
        case None =>
          log.info("Creating device actor for {}",trackMsg.deviceId)
          val deviceActor = context.actorOf(Device.props(groupId,trackMsg.deviceId))
          context.watch(deviceActor)
          deviceIdToActor += trackMsg.deviceId -> deviceActor
          actorToDeviceId += deviceActor -> trackMsg.deviceId
          deviceActor.forward(trackMsg)
      }

    case RequestTrackDevice(`groupId`,deviceId) =>
      log.warning("Ignoring TrackDevice request for {}.This actor is responsible for {}",`groupId`,this.groupId)

    case RequestDeviceList(requestId) =>
      sender() ! ReplyDeviceList(requestId,deviceIdToActor.keySet)

    case Terminated(deviceActor) =>
      val deviceId = actorToDeviceId(deviceActor)
      log.info("Device actor for {} has been terminated",deviceId)
      actorToDeviceId -= deviceActor
      deviceIdToActor -= deviceId


  }
}
