
//Imports
import Settings._
import Dependencies._

//Sbt Log Level
logLevel := Level.Info

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val akkahttptools = (project in file("."))
  .settings(rootSettings: _*)
  .settings(libraryDependencies ++= rootDependencies)
  .enablePlugins(SonaType)
