package com.neo.sk.Example

import akka.actor.{Actor, ActorSystem, Props}


import scala.io.StdIn
/**
  * created by byf on 2019.7.11
  * 生命周期钩子preStart和postStop启动Actor和停止Actor
  *
  * */
object StartStopActor1{
  def props:Props = Props(new StartStopActor1)
}
class StartStopActor1 extends Actor{
  override def preStart(): Unit = {
    println("first started")
    context.actorOf(StartStopActor2.props,"second")
  }

  override def postStop(): Unit = println("first stopped")

  override def receive: Receive = {
    case "stop" => context.stop(self)
  }

}
object StartStopActor2{
  def props:Props = Props(new StartStopActor2)
}
class StartStopActor2 extends Actor{
  override def preStart(): Unit = {
    println("second started")
  }

  override def postStop(): Unit = println("second stopped")
  override def receive: Receive = Actor.emptyBehavior
}
object StartStopActor extends App{
  val system = ActorSystem("testSystem")
  val first = system.actorOf(StartStopActor1.props,"first")
  first ! "stop"
  try StdIn.readLine()
  finally system.terminate()
}