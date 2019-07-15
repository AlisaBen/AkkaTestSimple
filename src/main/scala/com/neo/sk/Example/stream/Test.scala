package com.neo.sk.Example.stream
import akka.{Done, NotUsed}
import akka.actor.ActorSystem

import scala.concurrent.Future
//import akka.actor.typed.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent._
import scala.concurrent.duration._


object Test extends App{
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  //第一种是元素的类型，该源发射，
  // 第二个可用信号通知运行源产生一些辅助的值
  // （例如一个网络源可提供关于绑定端口或对等方的地址信息）。
  // 如果没有产生辅助信息，akka.NotUsed则使用类型- 并且一个简单的整数范围肯定属于这一类。
  val source:Source[Int,NotUsed] = Source(1 to 100)

  //创建此源后意味着我们有一个如何发出前100个自然数的描述，
  // 但此源尚未激活。为了获得这些数字，我们必须运行它：
  //将数字打印到控制台 - 并将此小流设置传递给运行它的Actor
  //这Materializer是一个流执行引擎的工厂，它使流运行
  val done :Future[Done] = source.runForeach(i => println(i))(materializer)
  //ActorSystem它永远不会终止。幸运的是runForeach返回一个在流完成时解析的：Future[Done]
  implicit val ec = system.dispatcher
  //如果流解析完毕
  done.onComplete(_ => system.terminate())

}
