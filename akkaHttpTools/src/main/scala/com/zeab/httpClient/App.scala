package com.zeab.httpClient

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import com.zeab.webServer.WebServerActor2
import com.zeab.webServer.WebServerMessages.StartWebServer

object App extends App{

  implicit val actorSystem:ActorSystem = ActorSystem()

  actorSystem.actorOf(Props[WebServerActor2]) ! StartWebServer(Routes.generalRoute)
  //actorSystem.actorOf(Props[WebServerActor2]) ! StartWebServer(Routes.generalRoute)


}
