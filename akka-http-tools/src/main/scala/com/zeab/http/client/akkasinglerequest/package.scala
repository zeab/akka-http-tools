package com.zeab.http.client

//Imports
import com.zeab.http.seed._
import com.zeab.http._
//Akka
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
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

    private def asyncHttpResponse(url: String, method: String, body: Option[String] = None, headers: Option[Map[String, String]] = None, metaData: Option[Map[String, String]] = None)
                                 (implicit actorSystem:ActorSystem, ec:ExecutionContext, mat:ActorMaterializer): Future[Either[HttpError, HttpResponse]] = {
      val urlRequest = addUrl(url)
      val methodRequest = addMethod(method, urlRequest)
      //Calculate authorization if one is required
      val combineHeaders = authorization(url, method, body, headers, metaData).getOrElse(Map.empty) ++ headers.getOrElse(Map.empty)
      val completedHeaders =
        if(combineHeaders.isEmpty){
          None
        }
        else{
          Some(combineHeaders)
        }
      val headersRequest = addHeaders(completedHeaders, methodRequest)
      val bodyRequest = addBody(body, headersRequest)
      bodyRequest match {
        case Right(request) =>
          for {
            response <- Http().singleRequest(request)
            responseBody <- Unmarshal(response.entity).to[String]
          } yield Right(
            HttpResponse(
              url, method, body, completedHeaders, metaData,
              response.status.intValue(), Some(responseBody), Some(response.headers.map{header => header.name -> header.value}.toMap), 0))
          //TODO add the timer
        case Left(ex) =>
          Future(Left(
            HttpError(ex.toString,
              url,
              method,
              body,
              completedHeaders,
              metaData,
              0)))
          //TODO add the timer
      }}

    private def addUrl(url:String): Either[Throwable, HttpRequest] ={
      Try(HttpRequest(uri = url)) match {
        case Success(request) => Right(request)
        case Failure(ex) => Left(ex)}}

    private def addMethod(method:String, request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(r) =>
          method match {
            case seed.HttpMethods.get => Right(HttpRequest(uri = r.uri))
            case seed.HttpMethods.post => Right(HttpRequest(uri = r.uri, method = HttpMethods.POST))
            case seed.HttpMethods.put => Right(HttpRequest(uri = r.uri, method = HttpMethods.PUT))
            case seed.HttpMethods.delete => Right(HttpRequest(uri = r.uri, method = HttpMethods.DELETE))
            case seed.HttpMethods.trace => Right(HttpRequest(uri = r.uri, method = HttpMethods.TRACE))
            case _ => Left(new Throwable("Cant do that method"))
          }
        case Left(ex) => Left(ex)}}

    private def addHeaders(headers:Option[Map[String,String]], request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(req) =>
          headers match {
            case Some(unprocessedHeaders) =>
              val akkaHeaders = unprocessedHeaders.map{header =>
                val (headerKey, headerValue) = header
                RawHeader(headerKey, headerValue)}.toList
              Right(HttpRequest(uri = req.uri, method = req.method, headers = akkaHeaders))
            case None => Right(req)}
        case Left(ex) => Left(ex)}}

    private def addBody(body:Option[String], request:Either[Throwable, HttpRequest]): Either[Throwable, HttpRequest] ={
      request match {
        case Right(req) =>
          body match {
            case Some(bdy) => Right(HttpRequest(req.method, req.uri, req.headers, bdy))
            case None => Right(req)}
        case Left(ex) => Left(ex)}}

    def authorization(url: String, method: String, body: Option[String], headers:Option[Map[String,String]], metaData: Option[Map[String, String]]): Option[Map[String, String]] = HttpHeaders.authorizationBearerHeader(HttpSeedMetaData.bearerCheck(metaData))

  }

  object HttpClient extends HttpClient

}
