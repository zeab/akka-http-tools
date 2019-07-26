package zeab.akkahttptools.directives

//Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RejectionHandler, RequestContext}

trait DirectiveExtensions {

  def logRoute(idKey:String = "CorrelationId"): Directive[Unit] =
    extractActorSystem.flatMap{system: ActorSystem =>
      extractRequestContext.flatMap { ctx: RequestContext =>
        val startTime: Long = System.currentTimeMillis()
        //The id to look for in the headers for logging purposes
        val id: String = ctx.request.headers
          .find(_.name() == idKey)
          .map(_.value())
          .getOrElse("no-id")
        mapResponse { resp =>
          val totalTime: Long = System.currentTimeMillis() - startTime
          //Put the RouteTimerEvent into the event stream so anyone can connect and use how they please
          system.eventStream.publish(RouteTimerEvent(System.currentTimeMillis().toString, id, resp.status.intValue(), ctx.request.method.name, ctx.request.uri.toString(), totalTime))
          resp
        } & handleRejections {
          RejectionHandler.default
        }
      }
    }


}
