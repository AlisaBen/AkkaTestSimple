package com.neo.sk.Example.device

import akka.actor.{Actor, ActorLogging, Props}
import com.neo.sk.Example.device.Device.{RecordTemperature, TemperatureRecorded}
/**
  * created by byf on 2019.7.12
  * */
object Device {

  def props(groupId:String,deviceId:String):Props = Props(new Device(groupId,deviceId))

  final case class ReadTemperature(requestId:Long)
  final case class RespondTemperature(requestId:Long,value:Option[Double])

  final case class RecordTemperature(requestId:Long)
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

    case RecordTemperature(requestId) =>
      sender() ! TemperatureRecorded(requestId)
  }




}
