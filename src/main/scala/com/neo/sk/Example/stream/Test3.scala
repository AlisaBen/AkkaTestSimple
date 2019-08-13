package com.neo.sk.Example.stream

object Test3 {
  sealed trait WsMsgHost
  sealed trait WsMsgAudience
  sealed trait WsMsgClient extends WsMsgHost with WsMsgAudience
  case class Test() extends WsMsgHost
  def main(args: Array[String]): Unit = {
    val a = Test()
    println(a.isInstanceOf[WsMsgClient])
    println(a.isInstanceOf[WsMsgHost])
    println(a.isInstanceOf[WsMsgAudience])

    a.asInstanceOf[WsMsgClient]
//    if(a.isInstanceOf[WsMsgHost]){
//      a.asInstanceOf[WsMsgClient]
//    }

  }

}
