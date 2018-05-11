package com.zeab.akkaHttpClient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import com.zeab.httpSeed.HttpMethods.get
import com.zeab.httpSeed.HttpError

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait HttpClient{

  case class Url(url:String){
    //def toHttpResponse:Either[HttpError, HttpResponse] = httpResponse(url, get, None, None, None)
    def addMethod(method:String):Method = Method(url, method)
    def addBody(body:String):Body = Body(url, get, Some(body))
    def addHeaders(headers:Map[String,String]) = Headers(url, get, None, Some(headers))
    def addMetaData(metaData:Map[String,String]):MetaData = MetaData(url, get, None, None, Some(metaData))
  }

  case class Method(url:String, method:String){
    //def toHttpResponse:Either[HttpError, HttpResponse] = httpResponse(url, method, None, None, None)
    def addBody(body:String):Body = Body(url, method, Some(body))
    def addHeaders(headers:Map[String,String]) = Headers(url, method, None, Some(headers))
    def addMetaData(metaData:Map[String,String]):MetaData = MetaData(url, method, None, None, Some(metaData))
  }

  case class Body(url:String, method:String, body:Option[String]){
    //def toHttpResponse:Either[HttpError, HttpResponse] = httpResponse(url, method, body, None, None)
    def addHeaders(headers:Map[String,String]) = Headers(url, method, body, Some(headers))
    def addMetaData(metaData:Map[String,String]):MetaData = MetaData(url, method, body, None, Some(metaData))
  }

  case class Headers(url:String, method:String, body:Option[String], headers:Option[Map[String,String]]){
    //def toHttpResponse:Either[HttpError, HttpResponse] = httpResponse(url, method, body, headers, None)
    def addMetaData(metaData:Map[String,String]):MetaData = MetaData(url, method, body, headers, Some(metaData))
  }

  case class MetaData(url:String, method:String, body:Option[String], headers:Option[Map[String,String]], metaData:Option[Map[String,String]]){
    //def toHttpResponse:Either[HttpError, HttpResponse] = httpResponse(url, method, body, headers, metaData)
  }
  private def h1ttpResponse(url:String, method:String, body:Option[String], headers:Option[Map[String,String]], metaData:Option[Map[String,String]]):Either[HttpError, HttpResponse] = {

    implicit val system:ActorSystem = ActorSystem()
    implicit val mat:ActorMaterializer = ActorMaterializer()
    implicit val ec:ExecutionContext = system.dispatcher

    val gg = Http().singleRequest(HttpRequest(uri = url)).map(a => a)

    val sss = gg.value.getOrElse(Try(HttpResponse()))

    val hhh = sss.getOrElse(HttpResponse())


    val dd = Http().singleRequest(HttpRequest(uri = url))
      .onComplete {
        case Success(res) =>
          println(res)

        case Failure(_)   =>
          sys.error("something wrong")
      }



    ???
  }



}

object HttpClient extends HttpClient {

}
