import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "IncidentResponseManager"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.18",
    "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
    "org.apache.commons" % "commons-email" % "1.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
