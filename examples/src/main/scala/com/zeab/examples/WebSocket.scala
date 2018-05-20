package com.zeab.examples

import akka.actor.{ActorSystem, Props}
import com.zeab.web.server.WebServerActor
import com.zeab.web.server.WebServerMessages.StartWebServer

object WebSocket extends App {

  implicit val actorSystem:ActorSystem = ActorSystem()

  //actorSystem.actorOf(Props[WebServerActor]) ! StartWebServer()


}
