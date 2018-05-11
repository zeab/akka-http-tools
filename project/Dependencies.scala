
//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val seeds                     = "2.0.0-RC2"
    val akkaHttp                  = "10.1.0-RC1"
    val akka                      = "2.5.9"
  }

  //List of Dependencies
  val seeds                       = "zeab" %% "seeds" % V.seeds

  //Akka
  val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
  val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp

  //Group Common Dependencies
  val commonDependencies: Seq[ModuleID] = Seq(
    seeds,
    akkaStream,
    akkaHttp
  )

}

