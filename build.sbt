import sbt.Keys._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
name := "AkkaTestSimple"

val scalaV = "2.12.6"

val projectName = "AkkaTestSimple"
val projectVersion = "0.0.1"

def commonSettings = Seq(
  version := projectVersion,
  scalaVersion := scalaV,
  scalacOptions ++= Seq(
    //"-deprecation",
    "-feature"
  )
)
// Akka Http based backend
lazy val backend = (project in file("backend")).enablePlugins(PackPlugin)
  .settings(commonSettings: _*)
  .settings(name := "AkkaTestSimple")
  .settings(
    libraryDependencies ++= Dependencies.backendDependencies,
    libraryDependencies ++= Dependencies.testLibs
  )

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(name := "root")


