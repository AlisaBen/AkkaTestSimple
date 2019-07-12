package com.neo.sk.Example

import akka.actor.{Actor, ActorSystem, Props}

/**
  * created by byf on 2019.7.11
  * 处理异常
  *失败后，受监督的演员被停止并立即重新启动。
  * */
object SupervisingActor{
  def props:Props = Props(new SupervisingActor)
}

class SupervisingActor extends Actor{
  override def preStart(): Unit = println("supervising start")

  override def postStop(): Unit = println("supervising stopped")
  val child = context.actorOf(SupervisedActor.props,"supervised-actor")//被监督的ACTOR
  override def receive: Receive = {
    case "failChild" =>child ! "fail"
  }
}

object SupervisedActor{
  def props:Props = Props(new SupervisedActor)
}

class SupervisedActor extends Actor{
  override def preStart(): Unit = println("supervised start")

  override def postStop(): Unit = println("supervised stopped")

  override def receive: Receive = {
    case "fail" =>
      println("supervised actor fails now")
      throw new Exception("I failed!")
  }
}
object SupervisedActorExperiment extends App{
  val system = ActorSystem("actor-system")//root actor
  val supervisingActor = system.actorOf(SupervisingActor.props,"supervising-actor")//监督actor
  supervisingActor ! "failChild"//发送child 失败的消息

}
