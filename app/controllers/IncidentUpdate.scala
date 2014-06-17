package controllers 


import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime


object IncidentUpdate extends Controller with Secured{

  def incidentUpdate(incident_id: Int) = IsAuthenticated{
    user_id => implicit request =>
    Ok(views.html.incidentUpdateCreationView(updateForm, incident_id, user_id.toInt))
  }
  def markDeleted(id: Int) = IsAuthenticated{
    user_id => implicit request =>
    IncidentUpdateM.deleteUpdate(id)
    Ok("Succesfully Deleted")
  }


  val updateForm = Form(
    mapping (
      "incident_id" -> number,
      "description" -> nonEmptyText,
      "created_by" -> number
     )(
      (incident_id, description, created_by) => IncidentUpdateM(
      anorm.NotAssigned,
      incident_id,
      description,
      created_by,
      new DateTime(),
      false
      )
    )(update => Some(update.incident_id, update.description, update.created_by))
  )

  def submitUpdate(incident_id: Int) = IsAuthenticated{
    user_id =>
    implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.incidentUpdateCreationView(formWithErrors, incident_id, user_id.toInt))
      },
      value => {
        IncidentUpdateM.addIncidentUpdate(value)
        IncidentM.update(incident_id, user_id.toInt) //Set update time on each update
        IncidentM.getOneIncident(incident_id) match {
          case Some(incident) => {
            val incidentInfo = IncidentInfo(incident, 
              IncidentMeta(
                routes.IncidentView.incidentView(incident_id).absoluteURL(), 
                UserM.getUser(incident.created_by).get,
                UserM.getUser(incident.primary_responder).get,
                TeamM.getTeam(incident.respond_team_id.getOrElse(-1)),
                IncidentUpdateM.getIncidentUpdates(incident_id),
                List[String](),
                None
              )
            )
            Logger.info(s"Sending $incidentInfo")
            Emailer.send(incidentInfo)
          }
          case _ => Logger.error(s"Could not retrieve $incident_id to send notifications about it")
        }
        //Emailer.send(incident_id)
        Redirect(routes.IncidentView.incidentView(incident_id))
      }
    )
  }




}
