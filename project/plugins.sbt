logLevel := Level.Warn
//val sbtRevolverV = "0.8.0"
val sbtRevolverV = "0.9.1"
// val sbtAssemblyV = "0.13.0"
val sbtAssemblyV = "0.14.6"
//val sbtPackV = "0.9.2"
val sbtPackV = "0.10.1"
val sbtScalaJsV = "0.6.24"
//val coursierV = "1.0.0-M15"
//val coursierV = "1.0.0-RC3"
val buildinfoV = "0.6.1"

// `javacpp` are packaged with maven-plugin packaging, we need to make SBT aware that it should be added to class path.
classpathTypes += "maven-plugin"

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.5.0")

addSbtPlugin("io.spray" % "sbt-revolver" % sbtRevolverV)

//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % sbtAssemblyV)

addSbtPlugin("org.xerial.sbt" % "sbt-pack" % sbtPackV)

//addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % buildinfoV)

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")
