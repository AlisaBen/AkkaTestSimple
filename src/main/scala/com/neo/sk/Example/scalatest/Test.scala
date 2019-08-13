package com.neo.sk.Example.scalatest

import scala.collection.mutable

object Test {

  def test(a:Int)(implicit b:Int):Unit = {
    println(a + b)
  }
  def func(nameOpt:Option[String],name:String):Unit = {
    nameOpt match{
      case Some(`name`) =>
        println(`name`)
        println(name)
      case None =>
    }

  }

  def main(args: Array[String]): Unit = {
    implicit val r = 5
    test(4)
    val map = mutable.HashMap[Int,Int]()
    map.put(2,3)
    map.put(4,5)
    var ls = List.empty[Int]
    map.foreach { r =>
      ls = r._2 :: ls
    }
    println(ls)


//    func(Some("rr"),"gg")
//    func(None,"f")

    println("101#34".split("#").toList.head.toLong,"101#34".split("#").toList.drop(1).head.toLong)

    var lss:List[Long] = List(1)
    val a :Option[Long] = Some(3)



  }

}
