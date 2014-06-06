package controllers 


import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime
import anorm._



object IncidentBrowser extends Controller{

  def getIncidents(page: Int, sort: String, query: String, queryCol: String, queryOptions: Int) = Action{
    implicit request =>

    val incidents = IncidentM.getIncidents(page, sort, query, queryCol, queryOptions)
    Logger.info(s"Displaying ${incidents._1.size} incidents")

    incidents match {
      case (incidents, numberOfIncidents, err) =>
        Ok(views.html.incidentBrowser(incidents, numberOfIncidents, err, BrowserParams(page, sort, query, queryCol, queryOptions)))
    }
  }
    
    // ((incident: IncidentM) => Some(
    //   incident.title,
    //   incident.description,
    //   incident.incident_type,
    //   incident.status,
    //   incident.next_update_at,
    //   incident.iss


  //   (title, description, incident_type, status, next_update_at, 
  //       issue_type, issue_type_id, primary_responder, response_team,
  //       user_id) => 
  //     IncidentM(
  //       anorm.NotAssigned,
  //       title,
  //       description,
  //       incident_type,
  //       status,
  //       None,
  //       issue_type match {

  //       },
  //       response_team,
  //       primary_responder,
  //       None,
  //       None,
  //       formTimeToJoda(next_update_at),
  //       new DateTime(),
  //       new DateTime(),
  //       user_id,
  //       None
  //     )
  //   //   ))
  // )





}