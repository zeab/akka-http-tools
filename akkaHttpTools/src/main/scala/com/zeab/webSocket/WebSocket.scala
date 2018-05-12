package com.zeab.webSocket

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow

object WebSocket {

  def simpleWebSocket: Flow[Message, Message, _]  ={
    Flow[Message].map {
      case TextMessage.Strict(text) =>
        //Processing goes here
        TextMessage(s"I got your message: $text!")
      case _ =>
        //Error message here
        TextMessage(s"Sorry I didn't quite get that")
    }
  }

}


abstract class WebSocket {

  def incomingMsg:Message

}