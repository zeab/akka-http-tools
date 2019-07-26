//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val akka                        = "2.5.23"
    val akkaHttp                    = "10.1.9"
    val circe                       = "0.11.1"
    val scalaTest                   = "3.0.5"
    val aenea                       = "2.0.0-RC3"
  }

  //List of Dependencies
  val D = new {
    //Akka
    val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
    //Akka Http
    val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp
    //Json
    val circeCore                   = "io.circe" %% "circe-parser" % V.circe
    val circeParser                 = "io.circe" %% "circe-generic" % V.circe
    //Test
    val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"
    val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akka % Test
    //Xml
    val aenea                       = "com.github.zeab" %% "aenea" % V.aenea
  }

  val rootDependencies: Seq[ModuleID] = Seq(
    D.akkaHttp,
    D.circeCore,
    D.circeParser,
    D.scalaTest,
    D.akkaTestKit,
    D.aenea,
    D.akkaStream
  )

}
