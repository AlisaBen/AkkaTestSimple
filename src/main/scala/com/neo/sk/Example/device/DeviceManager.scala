package com.neo.sk.Example.device

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.neo.sk.Example.device.DeviceGroup
/**
  * created  by byf on 2019.7.12
  *
  * */
object DeviceManager{
  def props:Props = Props(new DeviceManager)

  final case class RequestTrackDevice(groupId:String,deviceId:String)
  case object DeviceRegistered
}
class DeviceManager extends Actor with ActorLogging{
  import com.neo.sk.Example.device.DeviceManager.RequestTrackDevice
  var groupIdToActor = Map.empty[String,ActorRef]
  var actorToGroupId = Map.empty[ActorRef,String]

  override def preStart(): Unit = log.info("DeviceManager started")

  override def postStop(): Unit = log.info("DeviceManager stopped")

  override def receive: Receive = {
    case trackMsg @ RequestTrackDevice(groupId,_) =>
      groupIdToActor.get(groupId) match {
        case Some(ref) =>
          ref.forward(trackMsg)//消息向下传递
        case None =>
          log.info("Creating device group actor for {}",groupId)
          val groupActor = context.actorOf(DeviceGroup.props(groupId),"group-" + groupId)
          context.watch(groupActor)
          groupActor.forward(trackMsg)
          groupIdToActor += groupId -> groupActor
          actorToGroupId += groupActor -> groupId
      }

    case Terminated(groupActor) =>
      val groupId = actorToGroupId(groupActor)
      log.info("Device group actor for {} has been terminated",groupId)
      actorToGroupId -= groupActor
      groupIdToActor -= groupId

  }


}
