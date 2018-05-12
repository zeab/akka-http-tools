package com.zeab.httpClient

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import com.zeab.webServer.WebServerActor
import com.zeab.webServer.WebServerMessages.StartWebServer
import com.zeab.webSocket.{WebSocket, WebSocket2, WebSocketActor}
import com.zeab.webSocket.WebSocketMessages.OpenWebSocket

object App extends App{

  implicit val actorSystem:ActorSystem = ActorSystem()

  actorSystem.actorOf(Props[WebServerActor]) ! StartWebServer(Routes.websocketRoute)

  //actorSystem.actorOf(Props[WebSocketActor]) ! OpenWebSocket()
  //}

  //actorSystem.actorOf(Props[WebServerActor2]) ! StartWebServer(Routes.generalRoute)

  val ss = new WebSocket2 {
    override def incomingMsg(text: String): Message = TextMessage(s"asdasd $text")
  }

  val ff = ss.simpleWebSocket

}
