name := "akka_simple"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.13" withSources ()
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.13"