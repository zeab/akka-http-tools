
//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val seeds                     = "4.0.0"
    val akkaHttp                  = "10.1.0-RC1"
    val akka                      = "2.5.9"
    val akkaTestKit               = "2.5.12"
    val scalaTest                 = "3.0.5"
  }

  //Units of Work
  val seeds                       = "zeab" %% "seeds" % V.seeds

  //Akka
  val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
  val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp

  //Test
  val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akkaTestKit % Test
  val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"

  //Group Common Dependencies
  val commonDependencies: Seq[ModuleID] = Seq(
    seeds,
    akkaStream,
    akkaHttp,
    akkaTestKit,
    scalaTest
  )

}

