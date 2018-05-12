package com.zeab.httpClient

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import com.zeab.webSocket.WebSocket2

import scala.concurrent.Future

object Routes {

  //General Routes that don't have anywhere else to live
  def generalRoute(implicit actorSystem:ActorSystem):Route = {
    //Simple routes to allow basic checks by client or service
    path("ingressCheck") {
      get {
        actorSystem.log.debug("Get Ingress Check Received")
        complete(StatusCodes.Accepted, "Get Works")
      } ~
        put {
          actorSystem.log.debug("PUT Ingress Check Received")
          complete(StatusCodes.Accepted, "Put Works")
        } ~
        post {
          actorSystem.log.debug("POST Ingress Check Received")
          complete(StatusCodes.Accepted, "Post Works")
        } ~
        delete {
          actorSystem.log.debug("DELETE Ingress Check Received")
          complete(StatusCodes.Accepted, "Delete Works")
        }
    }
  }

  def websocketRoute =
    path("ws") {
      get {
        val echoFlow: Flow[Message, Message, _] = Flow[Message].map {
          case TextMessage.Strict(text) => TextMessage(s"I got your message: $text!")
          case _ => TextMessage(s"Sorry I didn't quite get that")
        }
        handleWebSocketMessages( echoFlow)
      }
    }~
    path("ws1") {
      get {
        val ss = new WebSocket2 {
          override def incomingMsg(text: String): Message = TextMessage(s"asdasd $text")
        }
        handleWebSocketMessages( ss.simpleWebSocket)
      }
    }~
    path("ws2") {
      get {
        handleWebSocketMessages(MyWebSocket.simpleWebSocket)
      }
    }


}
