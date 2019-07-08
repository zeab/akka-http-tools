//package zeab.akkahttptools.httpclient
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.model.headers.RawHeader
//import akka.http.scaladsl.model.{HttpMethod, HttpMethods, HttpRequest}
//import akka.http.scaladsl.unmarshalling.Unmarshal
//import io.circe.{Decoder, Encoder}
//import zeab.httpseed.{HttpError, HttpResponse}
//
//import scala.concurrent.{ExecutionContext, Future}
//
////Imports
//import zeab.httpseed.HttpMethods.get
////Scala
//import scala.reflect.runtime.universe._
//import akka.http.scaladsl.Http
//
//trait HttpClient {
//
//  def invokeAsyncHttp[ReqBody, RespBody](
//                                          url: String,
//                                          method: String = get,
//                                          body: ReqBody = "",
//                                          headers: Map[String, String] = Map.empty,
//                                          metaData: Map[String, String] = Map.empty
//                                        )(
//                                          implicit executionContext: ExecutionContext,
//                                          actorSystem: ActorSystem,
//                                          encoder: Encoder[ReqBody],
//                                          decoder: Decoder[RespBody],
//                                          typeTag: TypeTag[RespBody]
//                                        ): Future[Either[HttpError, HttpResponse[RespBody]]] = {
//    val convertedMethod: HttpMethod = method.toUpperCase match {
//      case "GET" => HttpMethods.GET
//      case "POST" =>HttpMethods.POST
//      case "PUT" => HttpMethods.PUT
//      case "DELETE" => HttpMethods.DELETE
//    }
//
//    val completeHeaders: List[RawHeader] = headers.map{header =>
//      val (headerKey, headerValue): (String, String) = header
//      RawHeader(headerKey, headerValue)
//    }.toList
//
//    //Encode the body here so that its actually a value rather than toString'ing it
//
//    val request: HttpRequest = HttpRequest(convertedMethod, url, completeHeaders, body.toString)
//
//    for {
//      response <- Http().singleRequest(request)
//      responseBody <- Unmarshal(response.entity).to[String]
//    } yield Right(HttpResponse[String]("", response.status.intValue(), response.headers.map{header => header.name -> header.value}.toMap, Right(responseBody), responseBody, 1, url, method, body.toString, headers, metaData) )
//
//    //TODO an this is where i need to go back and clean this up properly so that it does the marshalling and unmarshalling correctly...
//    //TODO also so that it returns the right thing at the end rather than just a Future[Right]
//
//  }
//
//}
//
//object HttpClient extends HttpClient