package com.neo.sk.Example.stream

import java.nio.file.{Path, Paths}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString
import akka.stream.scaladsl._
import scala.concurrent.Future
//import akka.stream.typed.scaladsl.ActorMaterializer
import akka.stream._

object Test2 extends App {
  final case class Author(handle:String)
  final case class Hashtag(name:String)
  final case class Tweet(author: Author,timestamp:Long,body:String){
    def hashtags:Set[Hashtag] =
      body
      .split(" ")
      .collect{
        case t if t.startsWith("#") => Hashtag(t.replaceAll("[^#\\w]",""))
      }
      .toSet
  }
  val akkaTag = Hashtag("#akka")
  val tweets: Source[Tweet, NotUsed] = Source(
    Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
      Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
      Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
      Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
      Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
      Nil)

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  tweets.map(_.hashtags)
    .reduce(_ ++ _)
    .mapConcat(identity)
    .map(_.name.toUpperCase)
    .runWith(Sink.foreach(println))



}


