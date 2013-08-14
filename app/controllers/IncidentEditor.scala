package controllers 


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
          user_id.toInt //IGNORED
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
          val currentTime = new DateTime()
          //Logger.debug(currentTime.toString)

          //If closed, add finish time
          val finishedTime = value.status match {
            case "open" => None
            case "closed" => Some(currentTime)
          }

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
            AnormJoda.formTimeToJoda(value.next_update_at_string),
            currentTime, //IGNORED (created_at)
            currentTime,
            value.user_id, //IGNORED (created_by)
            Some(value.user_id) //Updater's user ID
          )
          IncidentM.editIncident(incident)
          Redirect(routes.IncidentBrowser.getIncidents())
      }
    )
  }


}
