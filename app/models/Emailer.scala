package models

import org.apache.commons.mail._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props


//case class Mail(subject: String, body: String, to: String, bcc: List[String])


object Emailer{
  val system = ActorSystem("EmailSystem")
  val email = system.actorOf(Props[Emailer], name = "emailer")

  def send(incident_id: Int) = {
    email ! incident_id
  } 
}


class Emailer extends Actor{

  def receive = {
    case incident_id: Int => sendEmail(incident_id)
    case _ => play.api.Logger.debug("Email not sent")
  }

  def sendEmail(incident_id: Int) = {
    IncidentM.getOneIncident(incident_id) match {
      case Some(incident) => {
        val subject = incident.title
        val msg = incident.description
        val subEmails = IncidentM.getAllEmails(incident_id)
        val primaryEmail = UserM.getUser(incident.primary_responder).map(_.email).getOrElse("")
        val responseTeam = TeamM.getUserEmails(incident.respond_team_id.getOrElse(-1))
        val ccList = (responseTeam ::: subEmails).map(new javax.mail.internet.InternetAddress(_))


        var email = new SimpleEmail()
        email.setHostName("smtp.googlemail.com")
        email.setSmtpPort(465)
        email.setAuthenticator(new DefaultAuthenticator("sogimaplayapp@gmail.com", "w00tw00t"))
        email.setFrom("sogimaplayapp@gmail.com")


        email.setSubject(subject)
        email.setMsg(msg)
        email.addTo(primaryEmail)

        import scala.collection.JavaConversions._
        import scala.collection.mutable.ListBuffer
        val javaEmails: java.util.List[javax.mail.internet.InternetAddress] = ListBuffer(ccList: _* )

        email.setCc(javaEmails)
        email.setSSL(true)
        email.setSSLOnConnect(true)
        email.setTLS(true)
        email.send()
      }
      case None => {
        play.api.Logger.debug("Email not sent")


      }
    }
   
  }

}




