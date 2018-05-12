package com.zeab.webSocket

//Imports
import akka.actor.ActorRef

object WebSocketMessages {

  case class OpenWebSocket()
  case class OpenedWebSocketOut(webSocketOut:ActorRef)

  case class IncomingWebSocketMsg(msg:String)
  case class OutgoingWebSocketMsg(msg:String)

}
