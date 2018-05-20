package com.zeab.web.socket

//Imports
import akka.NotUsed
import akka.actor.{ActorRef, PoisonPill}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}

object WebSocket {

  def broadcast(router:ActorRef): Flow[Message, Message, NotUsed] = {
    //Set up the incoming flow
    val incomingMessages: Sink[Message, NotUsed] =
      Flow[Message].map {
        case TextMessage.Strict(text) => text
        case _ => "Unsupported Msg Type"
      }.to(Sink.actorRef(router, PoisonPill))

    //Set up the outgoing flow
    val outgoingMessages: Source[Message, NotUsed] =
      Source.actorRef[String](100, OverflowStrategy.fail)
        .mapMaterializedValue { outActor =>
          // give the user actor a way to send messages out
          router ! outActor
          NotUsed
        }.map(
        // transform domain message to web socket message
        (outMsg: String) => TextMessage(outMsg))

    // then combine both to a flow
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }

}
