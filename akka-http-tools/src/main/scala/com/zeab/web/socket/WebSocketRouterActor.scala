package com.zeab.web.socket

//Imports
import akka.actor.{Actor, ActorLogging, ActorRef}

class WebSocketRouterActor extends Actor with ActorLogging{

  def receive: Receive = disconnected

  private def disconnected: Receive = {
    case message:ActorRef => context.become(connected(message))
    case message:String => self ! message
  }

  private def connected(ws:ActorRef): Receive = {
    case message:String =>
      ws ! processMessage(message)
  }

  def processMessage(message:String): String = s"echoing: $message"

  //Lifecycle Hooks
  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name}")
  }
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
  }

}
