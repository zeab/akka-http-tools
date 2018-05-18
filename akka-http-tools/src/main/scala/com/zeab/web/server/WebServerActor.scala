package com.zeab.web.server

//Imports
import com.zeab.web.server.WebServerMessages.{StartWebServer, StopWebServer}
//Akka
import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.stream.ActorMaterializer
import akka.pattern.pipe
//Akka Http
import akka.http.scaladsl.Http
//Scala
import scala.concurrent.ExecutionContext

/** A ready to go web server bundled inside a web server */
class WebServerActor extends Actor with ActorLogging {

  implicit val actorSystem: ActorSystem = context.system
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = context.dispatcher

  //Receive
  def receive: Receive = disconnected

  //Behaviors
  def disconnected: Receive = {
    case StopWebServer =>
      log.warning(s"Web Server ${self.path.name} is already disconnected")
    case message: StartWebServer =>
      log.info(s"Web Server ${self.path.name} starting binding attempt")
      context.become(connecting)
      Http().bindAndHandle(message.routes, message.host, message.port.toInt).pipeTo(self)
  }
  def connected(webServer: Http.ServerBinding): Receive = {
    case StopWebServer =>
      log.info(s"Web Server ${self.path.name} offline ${webServer.localAddress}")
      webServer.unbind()
      context.become(disconnected)
    case _: StartWebServer =>
      log.warning(s"Web Server ${self.path.name} already connected")
  }
  def connecting: Receive = {
    case _: StartWebServer =>
      log.warning(s"Web Server ${self.path.name} is already attempting to establish binding")
    case StopWebServer =>
      log.warning(s"Web Server ${self.path.name} is disconnecting while connecting")
      context.become(disconnected)
      self ! StopWebServer
    case message: Http.ServerBinding =>
      context.become(connected(message))
      log.info(s"Web Server ${self.path.name} online ${message.localAddress}")
  }

  //Lifecycle Hooks
  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name} and waiting for a StartWebServer")
  }
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
  }

}
