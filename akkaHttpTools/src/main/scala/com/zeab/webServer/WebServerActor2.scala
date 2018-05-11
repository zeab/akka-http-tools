package com.zeab.webServer

//Imports
import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.zeab.webServer.WebServerMessages.{StartWebServer, StopWebServer}
import akka.pattern.pipe

import scala.concurrent.ExecutionContext

class WebServerActor2 extends Actor with ActorLogging{

  implicit val actorSystem:ActorSystem = context.system
  implicit val mat:ActorMaterializer = ActorMaterializer()
  implicit val ec:ExecutionContext = context.dispatcher

  def receive: Receive = disconnected

  def disconnected: Receive ={
    case StopWebServer =>
      log.warning("Web Server is already disconnected")
    case message:StartWebServer =>
      Http().bindAndHandle(message.routes, message.host, message.port.toInt).pipeTo(self)
    case message:Http.ServerBinding =>
      context.become(connected(message))
      log.info(s"Server online ${message.localAddress}")
  }

  def connected(webServer:Http.ServerBinding): Receive ={
    case StopWebServer =>
      webServer.unbind()
      context.become(disconnected)
    case StartWebServer =>
      log.warning("Web Server already connected")
  }

  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name} and waiting for a StartWebServer")
  }
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
  }
}
