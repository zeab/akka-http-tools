package com.zeab.webSocket

//Imports
import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives.handleWebSocketMessages
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.zeab.webSocket.WebSocketMessages.{IncomingWebSocketMsg, OpenWebSocket, OpenedWebSocketOut}

class WebSocketActor extends Actor with ActorLogging{
  
  def receive = disconnected

  def disconnected: Receive ={
    case message:OpenWebSocket =>

      log.info("Web Socket Connecting")
      context.become(connecting)

      val incomingMessages: Sink[Message, NotUsed] =
        Flow[Message].map {
          case TextMessage.Strict(text) => IncomingWebSocketMsg(text)
          case _ => IncomingWebSocketMsg("Unsupported Msg Type please use Strict Text")
        }.to(Sink.actorRef(self, PoisonPill))

      val outgoingMessages: Source[Message, NotUsed] =
        Source.actorRef[String](100, OverflowStrategy.fail)
          .mapMaterializedValue { outActor =>
            // give the user actor a way to send messages out
            log.info("Sending out WebSocketOut ActorRef")
            self ! OpenedWebSocketOut(outActor)
            NotUsed
          }.map(
          // transform domain message to web socket message
          (outMsg: String) => TextMessage(outMsg))

      log.info("Becoming Open Flow")
      context.become(openFlow(Flow.fromSinkAndSource(incomingMessages, outgoingMessages)))
  }
  def connecting: Receive ={
    case message:OpenedWebSocketOut =>
      log.info("open web received")
      self ! message
  }
  def openFlow(flow:Flow[Message, Message, NotUsed]): Receive ={
    case message:OpenedWebSocketOut =>
      log.info("Becoming Connected")
      handleWebSocketMessages(flow)
      context.become(connected(flow, message.webSocketOut))
  }
  def connected(flow:Flow[Message, Message, NotUsed], webSocketOut:ActorRef): Receive ={
    case message:IncomingWebSocketMsg => println(message)
  }
  def disconnecting: Receive ={
    case _ => ""
  }

}
