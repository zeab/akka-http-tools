
//Imports
import Common._
import Dependencies._

lazy val akkaHttpTools = (project in file("akka-http-tools"))
  .settings(baseProjectSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(artifactory: _*)

lazy val examples = (project in file("examples"))
  .settings(baseProjectSettings: _*)
  .dependsOn(akkaHttpTools)
