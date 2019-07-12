import sbt._

/**
  * User: Taoz
  * Date: 6/13/2017
  * Time: 9:38 PM
  */
object Dependencies {

  val akkaV = "2.5.13"
  val akkaHttpV = "10.1.3"
  val circeVersion = "0.9.3"
  val diodeV = "1.1.2"

  val akkaSeq = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV withSources (),
    "com.typesafe.akka" %% "akka-actor-typed" % akkaV withSources (),
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-stream-typed" % akkaV
  )

  val akkaHttpSeq = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV
  )

  val circeSeq = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "2.16.0"
  val hikariCP = "com.zaxxer" % "HikariCP" % "2.6.2"
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val codec = "commons-codec" % "commons-codec" % "1.10"
  val asynchttpclient = "org.asynchttpclient" % "async-http-client" % "2.0.32"



  val backendDependencies: Seq[ModuleID] =
    Dependencies.akkaSeq ++
    Dependencies.akkaHttpSeq ++
    Dependencies.circeSeq ++
    Seq(
      Dependencies.nscalaTime,
      Dependencies.hikariCP,
      Dependencies.logback,
      Dependencies.codec,
      Dependencies.asynchttpclient
    )

  val testLibs = Seq(
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaV % "test",
    "org.scalactic" %% "scalactic" % "3.0.8",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test"
  )


}
