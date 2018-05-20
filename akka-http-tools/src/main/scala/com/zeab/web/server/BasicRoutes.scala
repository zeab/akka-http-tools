package com.zeab.web.server

//Imports
import com.zeab.web.socket.{WebSocket, WebSocketRouterActor}
//Akka
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object BasicRoutes {

  def allRoutes(implicit actorSystem: ActorSystem): Route = generalRoute ~ wsRoute

  def generalRoute(implicit actorSystem: ActorSystem): Route = {
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
    } ~
    path("timeout") {
      actorSystem.log.debug("Timeout Received")
      complete(StatusCodes.GatewayTimeout, "Gateway timeout")
    } ~
    path("error") {
      actorSystem.log.debug("Error Received")
      complete(StatusCodes.InternalServerError, "Error")
    }
  }

  def wsRoute(implicit actorSystem: ActorSystem): Route = {
    path("wsConnect") {
      get {
        val wsOut = actorSystem.actorOf(Props[WebSocketRouterActor])
        handleWebSocketMessages(WebSocket.broadcast(wsOut))
      }
    }
  }

}
