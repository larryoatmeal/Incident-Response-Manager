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
      "If Issue Type is not 'None', issue id is necessary. If you are the primary responder, you must specify an update time",
      i => 
        ((i.issue_type_id, i.issue_id) match {
          case (Some(1), None) => true //1 is "Direct". If issue type direct, issue id optional
          case (Some(issue_type), None) => false //If other issue type beside Direct must have issue_id
          case (None, None) => false //If no issue type must have issue_id
          case (_, _) => true //anything else is fine     
        }) && !(i.user_id == i.primary_responder && i.next_update_at_string.isEmpty)
        //If user is primary responsder, must supply update time
        
    )
  )
  
  def addIncident = IsAuthenticated {
    user_id => implicit request => {
      val newForm = IncidentFormTemp("","","","",None, None, None, 0, None, user_id.toInt, List[Int]())
      Ok(views.html.incidentCreationForm(incidentForm.fill(newForm)))
    }
  }

  def submitIncidentForm = IsAuthenticated {
    user_id => implicit request =>
    incidentForm.bindFromRequest.fold(
      formWithErrors =>{
          Ok(views.html.incidentCreationForm(formWithErrors))
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
            Some(currentTime),
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

          Logger.info("subscriptions: %s".format(value.subscriptions))

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
          
          IncidentM.getOneIncident(incident_id) match {
            case Some(incident) => {
              val incidentInfo = IncidentInfo(incident, 
                IncidentMeta(
                  routes.IncidentView.incidentView(incident_id).absoluteURL(), 
                  UserM.getUser(incident.created_by).get,
                  UserM.getUser(incident.primary_responder).get,
                  TeamM.getTeam(incident.respond_team_id.getOrElse(-1)),
                  List[IncidentUpdateM](),
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




          Redirect(routes.IncidentBrowser.getIncidents()).flashing(
              "message" -> "Incident added",
              "category" -> Helper.Success
            )
          }
      }
    )
  }
}
