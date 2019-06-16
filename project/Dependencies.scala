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
  }

  //List of Dependencies
  val D = new {
    //Akka Http
    val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp
    //Akka Kafka
    //val akkaKafka                   = "com.typesafe.akka" %% "akka-stream-kafka" % V.akkaKafka
    //Json
    val circeCore                   = "io.circe" %% "circe-parser" % V.circe
    val circeParser                 = "io.circe" %% "circe-generic" % V.circe
    //val akkaHttpCirce               = "de.heikoseeberger" %% "akka-http-circe" % V.akkaHttpCirce
    //Logging
    val akkaSlf4j                   = "com.typesafe.akka" %% "akka-slf4j" % V.akka
    //val logback                     = "ch.qos.logback" % "logback-classic" % V.logback
    //val logbackJson                 = "net.logstash.logback" % "logstash-logback-encoder" % V.logbackJson
    //Test
    val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"
    val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akka % Test
    //Scala XML
    //val scalaXML                    = "org.scala-lang.modules" %% "scala-xml" % V.scalaXML
    //Cassandra
    //val datastax                    = "com.datastax.cassandra" % "cassandra-driver-core" % V.datastax
    //ZooKeeper
    //val zooKeeper                   = "org.apache.zookeeper" % "zookeeper" % V.zooKeeper
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
    D.akkaSlf4j
  )

}
