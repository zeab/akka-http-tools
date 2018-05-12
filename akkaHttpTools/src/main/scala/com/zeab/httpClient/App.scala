package com.zeab.httpClient

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import com.zeab.webServer.WebServerActor
import com.zeab.webServer.WebServerMessages.StartWebServer

object App extends App{

  implicit val actorSystem:ActorSystem = ActorSystem()

  val x = actorSystem.actorOf(Props[WebServerActor])
  x ! StartWebServer(Routes.generalRoute, "8080", "8.8.8.8")

  //Thread.sleep(2000)

  println("aa")
  //while(true){
    x ! StartWebServer(Routes.generalRoute, "8080", "8.8.8.8")
  //}

  //actorSystem.actorOf(Props[WebServerActor2]) ! StartWebServer(Routes.generalRoute)


}
