package com.neo.sk.Example.stream
import java.nio.file.Paths

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent.Future
//import akka.actor.typed.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent._
import scala.concurrent.duration._


object Test1 extends App{
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  //第一种是元素的类型，该源发射，
  // 第二个可用信号通知运行源产生一些辅助的值
  // （例如一个网络源可提供关于绑定端口或对等方的地址信息）。
  // 如果没有产生辅助信息，akka.NotUsed则使用类型- 并且一个简单的整数范围肯定属于这一类。
  val source:Source[Int,NotUsed] = Source(1 to 100)

  val factorials = source.scan(BigInt(1)){(acc,next) =>
    println(acc)
    println(next)
    acc * next}
//  val a = factorials.map(num => ByteString(s"$num\n"))
//  val result:Future[IOResult] = factorials.map(num => ByteString(s"$num\n"))
//    .runWith(FileIO.toPath(Paths.get("factorials.txt")))

//  val a = Flow[String].map(s => ByteString(s+"\n")).toMat(FileIO.toPath(Paths.get("")))
//  (Keep.right)
  def lineSink(filename:String):Sink[String,Future[IOResult]] =
    Flow[String].map(s => ByteString(s+"\n")).toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  factorials.map(_.toString).runWith(lineSink("factorial2.txt"))


}
