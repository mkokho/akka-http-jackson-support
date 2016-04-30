name := "akka-http-jackson-support"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "2.4.4",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.3",

  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

publishTo := Some("Bintray" at "https://dl.bintray.com/mkokho/maven")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")