package com.zeab.examples

//Imports
import com.zeab.web.socket.WebSocketRouterActor
//Akka
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object WebSocketRoutes {

  def wsRoute(implicit actorSystem: ActorSystem): Route = {
    path("wsConnect") {
      get {
        //This should be an actor the extends over thus actor and overrides the processMessage
        val wsOut = actorSystem.actorOf(Props[WebSocketRouterActor])
        handleWebSocketMessages(com.zeab.web.socket.WebSocket.broadcast(wsOut))}}}

}
