package com.neo.sk.Example.device

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import com.neo.sk.Example.device.Device.{RecordTemperature, TemperatureRecorded}
import org.scalatest._
/**
  * created by byf on 2019.7.12
  * */
object Device {

  def props(groupId:String,deviceId:String):Props = Props(new Device(groupId,deviceId))

  final case class ReadTemperature(requestId:Long)
  final case class RespondTemperature(requestId:Long,value:Option[Double])

  final case class RecordTemperature(requestId:Long,value: Double)
  final case class TemperatureRecorded(requestId:Long)

}

class Device(groupId:String,deviceId:String) extends Actor with ActorLogging{

  //引入半生对象中的类
  import com.neo.sk.Example.device.Device.{ReadTemperature, RespondTemperature}

  var lastTemperatureReading:Option[Double] = None //最新的温度

  override def preStart(): Unit = log.info("Device actor {}-{} started",groupId,deviceId)

  override def postStop(): Unit = log.info("Device actor {}-{} stopped",groupId,deviceId)

  override def receive: Receive = {
    case ReadTemperature(requestId) =>
      //sender()来自继承的Actor
      sender() ! RespondTemperature(requestId,lastTemperatureReading)

    case RecordTemperature(requestId,value) =>
      lastTemperatureReading = Some(value)
      sender() ! TemperatureRecorded(requestId)
  }

}

object Test
  extends App
  with Matchers
  with WordSpecLike
//  with BeforeAndAfterAll
{
  "" in {
    val system = ActorSystem("device-system")
    val probe = new TestProbe(system)
    //  val system = ActorSystem("device-system")
    val deviceActor = system.actorOf(Device.props("group","device"))
    //  deviceActor ! Device.ReadTemperature(42)

    deviceActor.tell(Device.ReadTemperature(42),probe.ref)
    val response = probe.expectMsgType[Device.RespondTemperature]
    response.requestId should === (42L)
    response.value should === (None)

    deviceActor.tell(Device.RecordTemperature(1,32),probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(1))

    deviceActor.tell(Device.ReadTemperature(1),probe.ref)
    val response2 = probe.expectMsgType[Device.RespondTemperature]
    response2.requestId should === (1L)
    response2.value should === (32)
  }




}
