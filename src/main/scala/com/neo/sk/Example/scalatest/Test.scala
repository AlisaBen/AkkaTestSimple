package com.neo.sk.Example.scalatest

object Test {

  def test(a:Int)(implicit b:Int) = {
    println(a + b)
  }

  def main(args: Array[String]): Unit = {
    implicit val r = 5
    test(4)
  }

}
