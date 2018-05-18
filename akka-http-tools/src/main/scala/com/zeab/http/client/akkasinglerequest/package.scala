package com.zeab.http.client

//Imports
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.zeab.http.seed.{HttpError, HttpResponse, HttpSeed}
import com.zeab.http._
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
//Scala
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

package object akkasinglerequest {

  trait HttpClient {

    case class Seed(httpSeed:HttpSeed){
      def toAsyncHttpResponse(implicit actorSystem:ActorSystem, ec:ExecutionContext, mat:ActorMaterializer): Future[Either[HttpError, HttpResponse]] = asyncHttpResponse(httpSeed.url, httpSeed.method, httpSeed.body, httpSeed.headers, httpSeed.metaData)
    }

    private def asyncHttpResponse(url: String, method: String, body: Option[String] = None, headers: Option[Map[String, String]] = None, metaData: Option[Map[String, String]] = None)(implicit actorSystem:ActorSystem, ec:ExecutionContext, mat:ActorMaterializer): Future[Either[HttpError, HttpResponse]] = {
      val urlRequest = addUrl(url)
      val methodRequest = addMethod(method, urlRequest)
      val headersRequest = addHeaders(headers, methodRequest)
      val bodyRequest = addBody(body, headersRequest)
      bodyRequest match {
        case Right(request) =>
          for {
            response <- Http().singleRequest(request)
            responseBody <- Unmarshal(response.entity).to[String]
          } yield Right(
            HttpResponse(
              url,
              method,
              body,
              headers,
              metaData,
              response.status.intValue(),
              Some(responseBody),
              Some(response.headers.toString),
              0))
        case Left(ex) =>
          Future(Left(
            HttpError(ex.toString,
              url,
              method,
              body,
              headers,
              metaData,
              0)))
      }
    }

    private def addUrl(url:String): Either[Throwable, HttpRequest] ={
      Try(HttpRequest(uri = url)) match {
        case Success(request) =>
          Right(request)
        case Failure(ex) =>
          Left(ex)
      }
    }

    private def addMethod(method:String, request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(r) =>
          method match {
            case seed.HttpMethods.get =>
              Right(HttpRequest(uri = r.uri))
            case seed.HttpMethods.post =>
              Right(HttpRequest(uri = r.uri, method = HttpMethods.POST))
            case seed.HttpMethods.put =>
              Right(HttpRequest(uri = r.uri, method = HttpMethods.PUT))
            case _ =>
              Left(new Throwable("Cant do that method"))
          }
        case Left(ex) =>
          Left(ex)
      }
    }

    private def addHeaders(headers:Option[Map[String,String]], request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(r) =>
          headers match {
            case Some(h) =>
              //TODO add the headers here
              request
            case None =>
              request
          }
        case Left(ex) =>
          Left(ex)
      }
    }

    private def addBody(body:Option[String], request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(r) =>
          body match {
            case Some(h) =>
              //TODO add the body here
              request
            case None =>
              request
          }
        case Left(ex) =>
          Left(ex)
      }
    }

    def authorization = ???
  }

  object HttpClient extends HttpClient

}
