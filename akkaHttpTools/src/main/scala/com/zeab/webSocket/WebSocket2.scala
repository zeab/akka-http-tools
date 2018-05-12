package com.zeab.webSocket

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow

abstract class WebSocket2 {

  def simpleWebSocket: Flow[Message, Message, _]  ={
    Flow[Message].map {
      case TextMessage.Strict(text) => incomingMsg(text)
      case _ => outgoingMsg()
    }
  }

  def incomingMsg(text:String): Message
  def outgoingMsg(text:String = "Not a Strict TextMessage"): Message = TextMessage(text)

}
