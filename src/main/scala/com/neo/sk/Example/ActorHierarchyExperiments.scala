package com.neo.sk.Example

import akka.actor.{Actor,ActorSystem,Props}
import scala.io.StdIn

/**
  * created by byf on 2019.7.11
  * 层次结构的一个重要作用是安全地管理actor的生命周期
  * */

object PrintMyActorRefActor{
  /**
    * 伴生类，
    * 在Actor伴生对象内提供Props的工厂方法
    * */
  def props:Props = Props(new PrintMyActorRefActor)//Actor实例的工厂模式
}

class PrintMyActorRefActor extends Actor{
  /**
    * 继承于Actor的类，需要重写receive方法
    * 定义行为的反应
    *
    * */
  override def receive: Receive = {
    case "printit" =>
      val secondRef = context.actorOf(Props.empty,"second-actor")
      println(s"second:${secondRef}")
  }
}


object ActorHierarchyExperiments extends App {

  def byValue(str:String) = {
    println("a")
    println(str)
    println("b")
  }

  def byName(str : => String) = {
    println("a")
//    println(str)
    println("b")
  }

  byValue{
    println("s")
    "hello"
  }
  println("========")
  byName{
    println("s")
    "hello"
  }
  val system = ActorSystem("testSystem")
  val firstRef = system.actorOf(PrintMyActorRefActor.props,"first-actor")
  println(s"first:$firstRef")
  firstRef ! "printit"
  try StdIn.readLine()
  finally system.terminate()

}
