package com.zeab.httpClient

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

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

}
