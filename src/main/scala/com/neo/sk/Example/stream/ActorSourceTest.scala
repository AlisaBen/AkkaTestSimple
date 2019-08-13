package com.neo.sk.Example.stream
import akka.actor.ActorSystem
import akka.actor.typed.ActorRef
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.typed.scaladsl.ActorSource

object ActorSourceTest {

  trait Protocol
  case class Message(msg: String) extends Protocol
  case object Complete extends Protocol
  case class Fail(ex: Exception) extends Protocol
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  val source: Source[Protocol, ActorRef[Protocol]] = ActorSource.actorRef[Protocol](completionMatcher = {
    case Complete =>
      println("ss")
  }, failureMatcher = {
    case Fail(ex) => ex
  }, bufferSize = 8, overflowStrategy = OverflowStrategy.fail)

  val ref = source
    .collect {
      case Message(msg) => msg
    }
    .to(Sink.foreach(println))
    .run()

//  ref ! Message("msg1")

  def main(args: Array[String]): Unit = {
    ref ! Message("msg1")

  }
  // ref ! "msg2" Does not compile

}
