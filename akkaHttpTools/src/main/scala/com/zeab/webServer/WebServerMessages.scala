package com.zeab.webServer

//Imports
import akka.http.scaladsl.server.Route

/***/
object WebServerMessages {

  /***/
  case class StartWebServer(routes:Route, port:String = "8080", host:String = "0.0.0.0")
  /***/
  case object StopWebServer

}
