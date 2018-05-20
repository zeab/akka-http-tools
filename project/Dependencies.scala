
//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val akkaHttp                    = "10.1.1"
    val akka                        = "2.5.12"
    val scalaTest                   = "3.0.5"
    val seeds                       = "4.0.7"
  }

  //List of Dependencies
  val D = new {
    //Seed
    val seeds                       = "zeab" %% "seeds" % V.seeds
    //Akka
    val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
    val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp
    //Test
    val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akka % Test
    val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"
  }

  //Group Common Dependencies
  val commonDependencies: Seq[ModuleID] = Seq(
    D.seeds,
    D.akkaStream,
    D.akkaHttp,
    D.akkaTestKit,
    D.scalaTest
  )

}

