package controllers 

import scala.collection.mutable.ListBuffer

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime

//Case class specifically for handling form


object IncidentEditor extends Controller with Secured{
  

  def incidentEdit(incident_id: Int) = IsAuthenticated {
    user_id => implicit request =>

    //Fill form out
    IncidentM.getOneIncident(incident_id) match {
      case Some(incident) => {
        val filledForm = IncidentFormTemp(
          incident.title,
          incident.description,
          incident.incident_type,
          incident.status,
          AnormJoda.jodaToFormTime(incident.next_update_at),
          incident.issue_type_id,
          incident.issue_id,
          incident.primary_responder,
          incident.respond_team_id,
          user_id.toInt, //IGNORED
          //Get subscriptions linked to this incident
          IncidentSubscriptionsMap.getTeams(incident_id).map(_.id.get)
        )

        Ok(views.html.incidentEditorView(IncidentCreator.incidentForm.fill(filledForm), incident_id))  
      }
      case None => {
        Ok("error")
      }
    }
  }
  
  def submitIncidentEditForm(incident_id: Int) = IsAuthenticated {
    user_id => implicit request =>
    IncidentCreator.incidentForm.bindFromRequest.fold(
      formWithErrors =>{
        Logger.error("Invalid edit form")
        Ok(views.html.incidentEditorView(formWithErrors, incident_id))
      },
      value => {
        IncidentM.getOneIncident(incident_id) match {
          case Some(oldIncident) => {
            val currentTime = new DateTime()

            // got the previously saved incident so we can make the 
            // email message highlight what changed

            val messages = ListBuffer[String]()
            val incidentStatusChange = if (value.status != oldIncident.status) {
              messages += s"Incident status changed to '${value.status}'"
              val transition = (oldIncident.status, value.status)
              IncidentStatusChange.transitions.get(transition) 
            } else {
              None
            }
              

            //If closed, add finish time
            val finishedTime = value.status match {
              case "open" => None
              case "closed" => Some(currentTime)
            }

            //Check if subscriptions have changed
            val newSubscriptions = value.subscriptions.toSet
            val oldSubscriptions = IncidentSubscriptionsMap.getTeams(incident_id).map(_.id.get).toSet

            //Find additions:
            val additions = newSubscriptions.filter(!oldSubscriptions.contains(_))
            //Find deletions
            val deletions = oldSubscriptions.filter(!newSubscriptions.contains(_))

            //Add keys
            additions.foreach{
              team_id =>  {
                IncidentSubscriptionsMap.addSubscription(incident_id, team_id)
                TeamM.getTeam(team_id) match {
                  case Some(team) => {
                    messages += s"Subscribed team '${team.name}' to notifications concerning this incident"
                  }
                  case None =>
                }
              }
            }
            //Delete keys
            deletions.foreach{
              team_id => {
                IncidentSubscriptionsMap.deleteSubscription(incident_id, team_id) 
                TeamM.getTeam(team_id) match {
                  case Some(team) => {
                    messages += s"Unsubscribed team '${team.name}' to notifications concerning this incident"
                  }
                  case None =>
                }
              }
            }

            if (oldIncident.title != value.title)
              messages += s"Name of this incident changed, was\n'${oldIncident.title}'"
            if (oldIncident.description != value.description)
              messages += s"Description of this incident changed, was\n'${oldIncident.description}'"
            if (oldIncident.incident_type != value.incident_type)
              messages += s"Impact scope of this incident changed, was '${oldIncident.incident_type}'"
            if (oldIncident.primary_responder != value.primary_responder) {
              UserM.getUser(oldIncident.primary_responder) match {
                case Some(user) => messages += s"Primary responder of this incident changed, was\n'${user.first_name} ${user.last_name}'"
                case None => // wtf
              }
            }
            if (oldIncident.respond_team_id != value.response_team) {
              oldIncident.respond_team_id match {
                case Some(team_id) => {
                  TeamM.getTeam(team_id) match {
                    case Some(team) => {
                      messages += s"Response team for this incident changed, was team '${team.name}'"
                    }
                    case None => // wtf
                  }
                }
                case None => messages += s"Added response team for this incident"
              }
            }

            if (messages.length > 0)
              "CHANGES:" +=: messages 

            //Some fields are ignored in the SQL update
            val incident = IncidentM(
              anorm.Id(incident_id), 
              value.title,
              value.description,
              value.incident_type,
              value.status,
              value.issue_id,
              value.issue_type_id,
              value.response_team,
              value.primary_responder,
              None,
              finishedTime,
              if(finishedTime.isEmpty){//If incident closed, null out next update at time
                AnormJoda.formTimeToJoda(value.next_update_at_string)}
              else{
                None
              },
              currentTime, //IGNORED (created_at)
              currentTime,
              value.user_id, //IGNORED (created_by)
              Some(value.user_id) //Updater's user ID
            )
            val result = IncidentM.editIncident(incident)
            val incidentInfo = IncidentInfo(incident, 
              IncidentMeta(
                routes.IncidentView.incidentView(incident_id).absoluteURL(), 
                UserM.getUser(incident.created_by).get,
                UserM.getUser(incident.primary_responder).get,
                TeamM.getTeam(incident.respond_team_id.getOrElse(-1)),
                IncidentUpdateM.getIncidentUpdates(incident_id),
                messages.toList,
                incidentStatusChange
              )
            )
            Logger.info(s"Sending $incidentInfo")
            Emailer.send(incidentInfo)
            Redirect(routes.IncidentView.incidentView(incident_id)).flashing(
              "message" -> result.text,
              "category" -> result.category
            )
          }
          case None => {
            Ok("error")
          }
        }
      }
    )
  }

  def deleteIncidentSubscription(incident_id: Int, team_id: Int) = Action{
    implicit request =>
    IncidentSubscriptionsMap.deleteSubscription(incident_id, team_id)
    Ok("Ok")
  }

  def addIncidentSubscription(incident_id: Int, team_id: Int) = Action{
    implicit request =>
    IncidentSubscriptionsMap.addSubscription(incident_id, team_id)
    Ok("Ok")
  }

  


}
