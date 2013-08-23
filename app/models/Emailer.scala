package models

import org.apache.commons.mail._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import play.api.Logger
import play.api._
import play.api.mvc._


abstract class Mail{
  val incident_id: Int
}

  
case class IncidentCreatedMail(incident_id: Int) extends Mail
case class IncidentUpdatedMail(incident_id: Int, update: IncidentUpdateM) extends Mail
case class IncidentEditedMail(incident_id: Int, oldIncident: IncidentM) extends Mail
case class IncidentUnsubscribedMail(incident_id: Int, unsubscribed: Set[Int]) extends Mail
case class IncidentNewlySubscribedMail(incident_id: Int, newlysubscribed: Set[Int]) extends Mail



object Emailer{
  //Domain
  val domain = "http://localhost:9000"

  //Email config: Edit this
  val hostEmail = "youremail"
  val hostPassword = "yourpassword"
  val hostName = "hostname" //"smtp.gmail.com"
  val hostSmtpPort = 465
  val hostSSL = true
  val hostTLS = true 

  
  val system = ActorSystem("EmailSystem")
  val email = system.actorOf(Props[Emailer], name = "emailer")

  def send(mail: Mail) = {
    email ! mail
  } 
}


class Emailer extends Actor{

  def receive = {
    case mail: Mail => sendEmail(mail)
    case _ => play.api.Logger.debug("Email not sent")
  }








  def sendEmail(mail: Mail) = {
    val incident_id = mail.incident_id
    

    

    IncidentM.getOneIncident(incident_id) match {
      case Some(incident) => {

        val title = incident.title
        val description = incident.description
        val createdBy = UserM.getUserFullName(incident.created_by).getOrElse("Unknown")
        val createdAt = AnormJoda.formatDate(incident.created_at)
        val nextUpdate = AnormJoda.formatDate(incident.next_update_at).getOrElse("Not set")

        val primaryResponder = UserM.getUser(incident.primary_responder)
        val primaryResponderName = primaryResponder.map(user => user.first_name + " " + user.last_name).getOrElse("Unknown")
        val primaryResponderEmail = primaryResponder.map(_.email).getOrElse("")

        val responseTeam = incident.respond_team_id.flatMap(TeamM.getTeam(_))
        val responseTeamName = responseTeam.map(_.name).getOrElse("Unkown")
        val responseTeamEmails = TeamM.getUserEmails(incident.respond_team_id.getOrElse(-1))

        val subscriptionTeams = IncidentSubscriptionsMap.getTeams(incident_id).map(_.name)

        val subscriptionEmails = IncidentM.getAllEmails(incident_id)

        val ccList = (responseTeamEmails ::: subscriptionEmails)
        val incidentURL = Emailer.domain + controllers.routes.IncidentView.incidentView(incident_id)


        //Email config
        val email = new HtmlEmail()
        email.setHostName(Emailer.hostName)
        email.setSmtpPort(Emailer.hostSmtpPort)
        email.setAuthenticator(new DefaultAuthenticator(Emailer.hostEmail, Emailer.hostPassword))
        email.setFrom(Emailer.hostEmail)
        email.setSSLOnConnect(Emailer.hostSSL)
        email.setStartTLSEnabled(Emailer.hostTLS)


        def scalaToJavaEmails(list: List[String]): java.util.List[javax.mail.internet.InternetAddress] = {
          import scala.collection.JavaConversions._
          import scala.collection.mutable.ListBuffer 

          val internetAddressList = list.map(new javax.mail.internet.InternetAddress(_))
          ListBuffer(internetAddressList: _* )

        }


        def configureRecipientsDefault() = {//Send to all users associated with incident. Not functional.
          //Primary responder
          email.addTo(primaryResponderEmail)
          //Subscriptions
          if (!ccList.isEmpty){
            email.setCc(scalaToJavaEmails(ccList))
          }
        }
        
        def htmlFormatter(text: String) = {
          s"""
            |<html>
            |<head></head>
            |<body>
            |<pre>
            |$text
            |</pre>
            |<a href="$incidentURL">View Incident</a> 
            |</body>
            |</html>
            """.stripMargin
        }

        
        mail match {
          case mail: IncidentCreatedMail => {
            val subject = s"New Incident: $title"
            val msgText = s"""
            |Description: $description

            |Created By: $createdBy
            |Created At: $createdAt
            |Primary Responder: $primaryResponderName
            |Response Team: $responseTeamName
            |Next Expected Update At: $nextUpdate
            """.stripMargin

            val msgHtml = htmlFormatter(msgText)

            //val primaryResponderTag = s"""You are the primary responder."""
            //val subscribedTag = s"""You are subscribed to this incident."""

            //Send emails
            email.setSubject(subject)
            email.setTextMsg(msgText)
            email.setHtmlMsg(msgHtml)

            configureRecipientsDefault()

            //Logger.debug(msgText)
            Logger.debug(msgHtml)

            email.send()
          }

          case mail: IncidentUpdatedMail => {
            val update = mail.update
            val created_by = UserM.getUserFullName(update.created_by).getOrElse("Unknown")
            val created_at = AnormJoda.formatDate(update.created_at)

            val subject = s"Incident '$title' Updated"
            val msgText = s"""
            |"${update.description}"
            |
            |- $created_by
            |
            |Created at $created_at
            """.stripMargin

            val msgHtml = htmlFormatter(msgText)


            //Send emails
            email.setSubject(subject)
            email.setTextMsg(msgText)
            email.setHtmlMsg(msgHtml)

            configureRecipientsDefault
            Logger.debug(msgHtml)

            email.send()
          } 

          case mail: IncidentUnsubscribedMail => {
            //Email unsubscribed users
            if (!mail.unsubscribed.isEmpty){
              val unsubscribedTeams = mail.unsubscribed.flatMap(TeamM.getTeam(_).map(_.name))
              val unsubscribedEmails = mail.unsubscribed.flatMap(TeamM.getUserEmails(_))


              val unsubscribedSubject = s"Unsubscribed from '$title'"
              val unsubscribedMsgText = s"""
              |You have been unsubscribed from the following incident:
              |Title: $title
              |Description: $description
              |
              |The following teams have been unsubscribed
              |${unsubscribedTeams.mkString("",",","")}
              """
              val unsubscribedMsgHtml = htmlFormatter(unsubscribedMsgText)

              email.setSubject(unsubscribedSubject)
              email.setTextMsg(unsubscribedMsgText)
              email.setHtmlMsg(unsubscribedMsgHtml)
              email.addTo(Emailer.hostEmail)
              if(!unsubscribedEmails.isEmpty){
                email.setCc(scalaToJavaEmails(unsubscribedEmails.toList))
              }
              email.send()
            }
          }
          case mail: IncidentNewlySubscribedMail => {
            //Email newly subscribed users
            if (!mail.newlysubscribed.isEmpty){
              val newlysubscribedTeams = mail.newlysubscribed.flatMap(TeamM.getTeam(_).map(_.name))
              val newlysubscribedEmails = mail.newlysubscribed.flatMap(TeamM.getUserEmails(_))


              val newlysubscribedSubject = s"Newly subscribed to '$title'"
              val newlysubscribedMsgText = s"""
              |You have been newly subscribed to the following incident:
              |Title: $title
              |Description: $description
              |
              |The following teams have been newly subscribed
              |${newlysubscribedTeams.mkString("",",","")}
              """
              val newlysubscribedMsgHtml = htmlFormatter(newlysubscribedMsgText)

              email.setSubject(newlysubscribedSubject)
              email.setTextMsg(newlysubscribedMsgText)
              email.setHtmlMsg(newlysubscribedMsgHtml)
              email.addTo(Emailer.hostEmail)
              if(!newlysubscribedEmails.isEmpty){
                email.setCc(scalaToJavaEmails(newlysubscribedEmails.toList))
              }
              email.send()
            }              
          }
          case mail: IncidentEditedMail => {
            val oldIncident = mail.oldIncident
            val editedIncident = IncidentM.getOneIncident(mail.incident_id)

            if (oldIncident != editedIncident){//Only if incident was actually changed
              editedIncident.foreach{
                edited => {
                  val editedSubject =s"Edited: '$title'"
                  val editedMsgText = s"""
                  |Incident '$title' has been edited
                  """
                  val editedMsgHtml = htmlFormatter(editedMsgText)

                  email.setSubject(editedSubject)
                  email.setTextMsg(editedMsgText)
                  email.setHtmlMsg(editedMsgHtml)

                  configureRecipientsDefault()
                  email.send()
                }
              }
            }
          }
        }









      }
      case None => {
        play.api.Logger.error("Email not sent")


      }
    }
   
  }

}




