package controllers 


import play.api._
import play.api.mvc._
import models._


object IncidentView extends Controller{

  def incidentView(id: Int) = Action{
    implicit request =>
    val incident = IncidentM.getOneIncident(id)
    Ok(views.html.incidentView(incident))
  }
}
