package com.zeab.httpClient

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import com.zeab.webSocket.WebSocket3

object MyWebSocket extends WebSocket3{

  def incomingMsg(text: String): Message = TextMessage(s"asdasd $text")

}
