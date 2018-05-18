package com.zeab.web.server

//Imports
import akka.http.scaladsl.server.Route

/**Collection of messages to handle the actor hosing the web binding*/
object WebServerMessages {

  /**Open a web binding with the following information
    * @param routes The Standard Route's for the web server to host
    * @param port The port the web server will be listening on
    * @param host The host the web server will be listening on*/
  case class StartWebServer(
                             routes:Route,
                             port:String = "8080",
                             host:String = "0.0.0.0"
                           )

  /**Halt the existing web binding inside the actor and shut down the actor*/
  case object StopWebServer

}
