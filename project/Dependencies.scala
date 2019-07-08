//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val akka                        = "2.5.22"
    val akkaHttp                    = "10.1.8"
    val circe                       = "0.11.1"
    val scalaTest                   = "3.0.5"
    val aenea                       = "1.0.0"
    val httpSeed                    = "1.0.+"
  }

  //List of Dependencies
  val D = new {
    //Akka
    val akka                        = "com.typesafe.akka" %% "akka-actor" % V.akka
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
    //HttpSeed
    val httpSeed                    = "com.github.zeab" %% "httpseed" % V.httpSeed
  }

  val rootDependencies: Seq[ModuleID] = Seq(
    D.akkaHttp,
    D.circeCore,
    D.circeParser,
    D.scalaTest,
    D.akkaTestKit,
    D.aenea,
    D.httpSeed,
    D.akka
  )

}
