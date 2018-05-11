package com.zeab.httpClient

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest

object App extends App{


  val ss = Http().singleRequest(HttpRequest( ))


}
