package zeab.akkahttptools.directives

case class RouteTimerEvent(
                            logTimestamp:String,
                            id: String,
                            responseCode: Int,
                            method: String,
                            url:String,
                            durationInMs: Double
                          )
