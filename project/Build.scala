import sbt._
import Keys._
import play.Project._
import com.typesafe.sbt.SbtStartScript

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

  val allSettings = Defaults.defaultSettings ++ SbtStartScript.startScriptForClassesSettings

  val main = play.Project(appName, appVersion, appDependencies, settings=allSettings).settings(
      // define the statements initially evaluated when entering 'console',
      // 'console-quick', or 'console-project'
      initialCommands := """
       |// make app resources accessible
       |Thread.currentThread.setContextClassLoader(getClass.getClassLoader)
       |import anorm._
       |import models._
       |import scala.collection.JavaConversions._
       |new play.core.StaticApplication(new java.io.File("."))
      """.stripMargin
  )
}
