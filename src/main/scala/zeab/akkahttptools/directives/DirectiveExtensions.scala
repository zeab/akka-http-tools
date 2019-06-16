package zeab.akkahttptools.directives

//Imports
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RejectionHandler, RequestContext}

trait DirectiveExtensions {

  val logRoute: Directive[Unit] =
    extractRequestContext.flatMap{ctx: RequestContext =>
      val startTime: Long = System.currentTimeMillis()
      val correlationId: String = ctx.request.headers
        .find(_.name() == "correlationId")
        .map(_.value())
        .getOrElse("no-correlation-id")
      mapResponse{resp =>
        val totalTime: Long = System.currentTimeMillis() - startTime
        ctx.log.info(s"correlationId: $correlationId [${resp.status.intValue()}] ${ctx.request.method.name} ${ctx.request.uri} took: ${totalTime}ms")
        resp
      } & handleRejections{ RejectionHandler.default }
    }

}
