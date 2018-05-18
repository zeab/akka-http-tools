package webServer

//Imports
import com.zeab.webServer.WebServerActor
import com.zeab.webServer.WebServerMessages.{StartWebServer, StopWebServer}
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
//Akka Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get}
import akka.http.scaladsl.server.Route
//Scala
import org.scalatest._

class WebServerTest extends TestKit(ActorSystem("WebServerSpec", ConfigFactory.parseString("""
  akka.loggers = ["akka.testkit.TestEventListener"]
  """)))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A web server should start disconnected" must {

    val webServerName:String = "WebServerActor"
    val webServerActor:ActorRef = system.actorOf(Props[WebServerActor], webServerName)
    val route:Route = { get { complete(StatusCodes.Accepted, "Get Works") } }

    "return a warning when attempting to stop again" in {
      EventFilter.warning(message = s"Web Server $webServerName is already disconnected",
        occurrences = 1).intercept {
        webServerActor ! StopWebServer
      }
    }
    "connect successfully when asked" in {
      EventFilter.info(message = s"Web Server $webServerName online /127.0.0.1:8080",
        occurrences = 1).intercept {
        webServerActor ! StartWebServer(route, "8080", "localhost")
      }
    }
    "reject a connection while connecting" in {
      EventFilter.warning(message = s"Web Server $webServerName already connected",
        occurrences = 1).intercept {
        webServerActor ! StartWebServer(route, "8080", "localhost")
      }
    }
    "disconnect and close the server when requested" in {
      EventFilter.info(message = s"Web Server $webServerName offline /127.0.0.1:8080",
        occurrences = 1).intercept {
        webServerActor ! StopWebServer
      }
    }
  }

  "A web server should start reject new connection attempts while connecting" must {

    val webServerName:String = "WebServerActor1"
    val webServerActor:ActorRef = system.actorOf(Props[WebServerActor], webServerName)
    val route:Route = { get { complete(StatusCodes.Accepted, "Get Works") } }

    EventFilter.warning(message = s"Web Server $webServerName is already attempting to establish binding",
      occurrences = 3).intercept {
      webServerActor ! StartWebServer(route, "8081", "localhost")
      webServerActor ! StartWebServer(route, "8081", "localhost")
      webServerActor ! StartWebServer(route, "8081", "localhost")
      webServerActor ! StartWebServer(route, "8081", "localhost")
      webServerActor ! StopWebServer
    }

  }

  "A web server should disconnect if requested while connecting" must {

    val webServerName:String = "WebServerActor2"
    val webServerActor:ActorRef = system.actorOf(Props[WebServerActor], webServerName)
    val route:Route = { get { complete(StatusCodes.Accepted, "Get Works") } }

    EventFilter.warning(message = s"Web Server $webServerName is disconnecting while connecting",
      occurrences = 1).intercept {
      webServerActor ! StartWebServer(route, "8082", "localhost")
      webServerActor ! StopWebServer
    }

  }

}
