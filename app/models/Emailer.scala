package models

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import play.api.Logger
import play.api.mvc._

import javax.mail.internet.InternetAddress
import javax.mail.Authenticator
import org.apache.commons.mail._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

import com.typesafe.config.ConfigFactory

object Emailer{
  val system = ActorSystem("EmailSystem")
  val email = system.actorOf(Props[Emailer], name = "emailer")

  /*
  def send(incident_id: Int) = {
    email ! incident_id
  } 
  */

  def send(incidentInfo: IncidentInfo) = {
    email ! incidentInfo
  } 
}

case class SmtpConfig(host: String, port: Int, useSsl: Boolean, from: String)

object SmtpConfig {
  val appConfig = ConfigFactory.load()
  // cupcake pattern
  val config: SmtpConfig = if (appConfig.hasPath("smtp.mock") && appConfig.getBoolean("smtp.mock")) {
    val host: String = "smtp.host"
    val port: Int = -1
    val useSsl: Boolean = false
    val from: String = "smtp.from"
    SmtpConfig(host, port, useSsl, from)
  } else {
    val host = appConfig.getString(s"smtp.host")
    val port = appConfig.getInt(s"smtp.port")
    val useSsl = appConfig.getBoolean(s"smtp.ssl")
    val from = appConfig.getString(s"smtp.from")
    SmtpConfig(host, port, useSsl, from)
  }
  def host    = config.host
  def port    = config.port
  def useSsl  = config.useSsl
  def from    = config.from
}


class Emailer extends Actor{

  def receive = {
    //case incidentId: Int => sendEmail(incidentId)
    case incidentInfo: IncidentInfo => sendIncidentEmail(incidentInfo)
    case _ => Logger.warn("Email not sent, no incidentId received")
  }

  /*
  def sendEmail(incidentId: Int, withAuthenticator: Option[Authenticator] = None) = {
    IncidentM.getOneIncident(incidentId) match {
      case Some(incident) => {
        val subject = incident.title
        val msg = incident.description
        val subEmails = IncidentM.getAllEmails(incidentId)
        val primaryEmail = UserM.getUser(incident.primary_responder).map(_.email).getOrElse("")
        val responseTeam = TeamM.getUserEmails(incident.respond_team_id.getOrElse(-1))
        val ccList = (responseTeam ::: subEmails).map(new InternetAddress(_)).distinct
        val javaEmails: java.util.List[InternetAddress] = ListBuffer(ccList: _* )
        var email = new SimpleEmail()
        withAuthenticator match {
          // email.setAuthenticator(new DefaultAuthenticator("emailaddress", "password"))
          case Some(auth) => email.setAuthenticator(auth)
          case None =>
        }
        email.setHostName(SmtpConfig.host)
        email.setSmtpPort(SmtpConfig.port)
        email.setFrom(SmtpConfig.from)
        email.setSubject(subject)
        email.setMsg(msg)
        email.addTo(primaryEmail)
        email.setCc(javaEmails)
        email.setSSLOnConnect(SmtpConfig.useSsl)
        Logger.debug(s"sendEmail sending $email")
        email.send()
      }
      case None => {
        Logger.warn("sendEmail: Email not sent for incidentId")
      }
    }
   
  }
  */

  def sendIncidentEmail(incidentInfo: IncidentInfo, withAuthenticator: Option[Authenticator] = None) = {
      val subject = getSubject(incidentInfo)
      //val msg = incidentInfo.incident.description
      val msg = getBody(incidentInfo)
      val subEmails = IncidentM.getAllEmails(incidentInfo.incident.id.getOrElse(-1))
      val primaryEmail = UserM.getUser(incidentInfo.incident.primary_responder).map(_.email).getOrElse("")
      val responseTeam = TeamM.getUserEmails(incidentInfo.incident.respond_team_id.getOrElse(-1))
      val ccList = (responseTeam ::: subEmails).map(new InternetAddress(_)).distinct
      val javaEmails: java.util.List[InternetAddress] = ListBuffer(ccList: _* )
      var email = new SimpleEmail()
      withAuthenticator match {
        // email.setAuthenticator(new DefaultAuthenticator("emailaddress", "password"))
        case Some(auth) => email.setAuthenticator(auth)
        case None =>
      }
      email.setHostName(SmtpConfig.host)
      email.setSmtpPort(SmtpConfig.port)
      email.setFrom(SmtpConfig.from)
      email.setSubject(subject)
      email.setMsg(msg)
      email.addTo(primaryEmail)
      if (! javaEmails.isEmpty) 
        email.setCc(javaEmails)
      email.setSSLOnConnect(SmtpConfig.useSsl)
      Logger.debug(s"sendEmail sending $email")
      email.send()
  }

  def getSubject(incidentInfo: IncidentInfo): String = {
    incidentInfo.incident.incident_type match {
      case "external" => s"[SITEIMPACT] ${incidentInfo.incident.title}"
      case _ => s"[HEADSUP] ${incidentInfo.incident.title}"
    }
  }

  def getBody(incidentInfo: IncidentInfo): String = {
    val responseTeamName = incidentInfo.meta.responseTeam match {
      case Some(responseTeam) => responseTeam.name
      case None => "None"
    }
    val latestUpdate = if (incidentInfo.meta.updates.isEmpty) {
      ""
    } else {
      s"LATEST UPDATE: ${incidentInfo.meta.updates.head.description}\n\n"
    }
    val messages = if (incidentInfo.meta.messages.isEmpty) {
      ""
    } else {
      incidentInfo.meta.messages.mkString("\n\n")
    }
    s"""
       |${latestUpdate}${incidentInfo.incident.description}
       |
       |${messages}
       |
       |reporter: ${incidentInfo.meta.reporter.first_name} ${incidentInfo.meta.reporter.last_name}
       |contact: ${incidentInfo.meta.primary.first_name} ${incidentInfo.meta.primary.last_name}
       |team: ${responseTeamName}
       |status: ${incidentInfo.incident.status}
       |last update: ${incidentInfo.incident.updated_at}
       |link: ${incidentInfo.meta.url}
      """.stripMargin
  }

}




