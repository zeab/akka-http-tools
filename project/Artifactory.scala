
//Imports
import sbt.Keys._
import sbt._

object Artifactory extends AutoPlugin {

  // Run on everything in the project automatically
  override def trigger = allRequirements

  override lazy val projectSettings:Seq[Def.Setting[_]] = getArtifactoryCredentials

  def getArtifactoryCredentials: Seq[Def.Setting[_]] ={
    val propRealm                  = sys.props.get("ARTIFACTORY_REALM")
    val propHost                   = sys.props.get("ARTIFACTORY_HOST")
    val propUser                   = sys.props.get("ARTIFACTORY_USER")
    val propPassword               = sys.props.get("ARTIFACTORY_PASSWORD").map(_.replaceAll("^'+|'+$", ""))
    val envRealm                   = sys.env.get("ARTIFACTORY_REALM")
    val envHost                    = sys.env.get("ARTIFACTORY_HOST")
    val envUser                    = sys.env.get("ARTIFACTORY_USER")
    val envPassword                = sys.env.get("ARTIFACTORY_PASSWORD").map(_.replaceAll("^'+|'+$", ""))
    val artifactoryCredentialsFile = Path.userHome / ".sbt" / "zeab.credentials"

    ((propRealm, propHost, propUser, propPassword), (envRealm, envHost, envUser, envPassword)) match {
      case ((Some(realm), Some(host), Some(user), Some(password)), (_, _, _, _)) =>
        Seq(
          credentials += Credentials(realm, host, user, password),
          publishTo := Some(realm at host)
        )
      case ((_, _, _, _), (Some(realm), Some(host), Some(user), Some(password))) =>
        Seq(
          credentials += Credentials(realm, host, user, password),
          publishTo := Some(realm at host)
        )
      case _ =>
        if(artifactoryCredentialsFile.exists()){
          Seq(
            credentials += Credentials(artifactoryCredentialsFile),
            publishTo := Some("Artifactory Realm" at "http://165.227.62.157:8081/artifactory/zeab")
          )
        }
        else{
          Seq.empty
        }
    }
  }

}
