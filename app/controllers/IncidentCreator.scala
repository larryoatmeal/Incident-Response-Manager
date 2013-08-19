package controllers 


import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime
import play.api.data.validation.Constraints


//Case class specifically for handling form
case class IncidentFormTemp(title: String, description: String, incident_type: String,
    status: String, next_update_at_string: Option[String], issue_type_id: Option[Int],
    issue_id: Option[String], primary_responder: Int, response_team: Option[Int], user_id: Int, subscriptions: List[Int])


object IncidentCreator extends Controller with Secured{
  
  val incidentForm = Form(
    mapping (
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "incident_type" -> nonEmptyText,
      "status" -> nonEmptyText,
      "next_update_at_string" -> optional(nonEmptyText.verifying(Constraints.pattern(AnormJoda.timePattern, error ="Time must be in MM/dd/yyyy hh:mm"))),
      "issue_type_id" -> optional(number),
      "issue_id" -> optional(nonEmptyText),
      "primary_responder" -> number,
      "response_team" -> optional(number),
      "user_id" -> number,
      "subscriptions" -> list(number)
    )(IncidentFormTemp.apply)(IncidentFormTemp.unapply).verifying(
      "If Issue Type is not 'None', issue id is necessary. If you are the primary responder, you must specify an upate time",
      i => if (!i.issue_type_id.isEmpty && i.issue_id.isEmpty){
        Logger.debug("Condition 1")
        false
      }else if(i.user_id == i.primary_responder && i.next_update_at_string.isEmpty){
        Logger.debug(i.next_update_at_string.getOrElse("NONE"))
        false
      }
      else{
        true
      }
    )
  )
  
  def addIncident = IsAuthenticated {
    user_id => implicit request =>
    Ok(views.html.incidentCreationForm(incidentForm, user_id.toInt))
    
  }
  
  def submitIncidentForm = IsAuthenticated {
    user_id => implicit request =>
    incidentForm.bindFromRequest.fold(
      formWithErrors =>{
          Ok(views.html.incidentCreationForm(formWithErrors, user_id.toInt))
          },
        value => {
          val currentTime = new DateTime()
          //Logger.debug(currentTime.toString)


          val incident = IncidentM(
            anorm.NotAssigned, 
            value.title,
            value.description,
            value.incident_type,
            value.status,
            value.issue_id,
            value.issue_type_id,
            value.response_team,
            value.primary_responder,
            None,
            None,
            AnormJoda.formTimeToJoda(value.next_update_at_string),
            currentTime,
            currentTime,
            value.user_id,
            None
          )
          //Get id of inserted incident
          val incident_id = IncidentM.addIncident(incident)
          //Logger.debug(incident_id.toString)



          //-1 means something went wrong
          if (incident_id == -1){
            Redirect(routes.IncidentBrowser.getIncidents()).flashing(
              "message" -> "Incident not added",
              "category" -> Helper.Error
            )
          }else{
            //Add supscriptions
            value.subscriptions.foreach{
              subscription => IncidentSubscriptionsMap.addSubscription(incident_id, subscription)
            }
            
          Emailer.send(incident_id)




          Redirect(routes.IncidentBrowser.getIncidents()).flashing(
              "message" -> "Incident added",
              "category" -> Helper.Success
            )
          }
      }
    )
  }







}
