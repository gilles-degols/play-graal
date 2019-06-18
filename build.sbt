name := "toto"
 
version := "1.0" 
      
lazy val `toto` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.8"

val akkaVersion = "2.5.21"
val akkaHttpVersion = "10.1.7"

libraryDependencies ++= Seq( guice )
enablePlugins(GraalVMNativeImagePlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.slf4j" % "slf4j-jdk14" % "1.7.26", // java.util.logging works mostly out-of-the-box with SubstrateVM

  "net.codingwell" %% "scala-guice" % "4.2.2", // To use inject easily

  // required for substitutions
  // make sure the version matches GraalVM version used to run native-image
  "com.oracle.substratevm" % "svm" % "19.0.0" % Provided,
)

graalVMNativeImageOptions ++= Seq(
  "-H:IncludeResources=.*conf",
  "-H:IncludeResources=.*\\.properties",
  "-H:ReflectionConfigurationFiles=" + baseDirectory.value / "graal" / "reflectconf-akka.json",
  "-H:ReflectionConfigurationFiles=" + baseDirectory.value / "graal" / "reflectconf-jul.json",
  "--enable-url-protocols=https,http",
  "-H:+ReportExceptionStackTraces",
  "--initialize-at-build-time",
  "--initialize-at-run-time=" +
    "com.typesafe.config.impl.ConfigImpl$EnvVariablesHolder," +
    "com.typesafe.config.impl.ConfigImpl$SystemPropertiesHolder",
  "--no-fallback"
)
