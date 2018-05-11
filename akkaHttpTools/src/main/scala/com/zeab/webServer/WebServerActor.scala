package com.zeab.webServer

//Imports
import akka.actor.{ActorLogging, PoisonPill}
import com.zeab.webServer.WebServerMessages.{StartWebServer, StopWebServer}
//Akka
import akka.http.scaladsl.Http
import akka.actor.{Actor, ActorSystem}
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class WebServerActor extends Actor with ActorLogging{

  implicit val system:ActorSystem = context.system
  implicit val mat:ActorMaterializer = ActorMaterializer()
  implicit val ec:ExecutionContext = system.dispatcher

  var openServer:Option[Future[Http.ServerBinding]] = None

  def receive = {
    case message:StartWebServer =>
      openServer match {
        case Some(_) =>
          log.warning("Web Server is already started")
        case None =>
          openServer = Some(Http().bindAndHandle(message.routes, message.host, message.port.toInt))
          log.info(s"Web Server online at http://${message.host}:${message.port}/")
      }
    case StopWebServer =>
      log.debug("Stop Web Server Received")
      self ! PoisonPill
  }

  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name} and waiting for a StartWebServer")
  }
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
    openServer match {
      case Some(webServer) =>
        webServer.onComplete(s => println(s.get.localAddress))
        webServer.flatMap(_.unbind())
      case None =>
        log.debug("Stop Web Server Received but no web server is available to stop")
    }
  }

}
