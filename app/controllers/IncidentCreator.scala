package controllers 


import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime
import anorm._

//Case class specifically for handling form
case class IncidentFormTemp(title: String, description: String, incident_type: String,
    status: String, next_update_at_string: Option[String], issue_type_id: Option[Int],
    issue_id: Option[String], primary_responder: Int, response_team: Option[Int], user_id: Int )


object IncidentCreator extends Controller{
    
  //Time conversions to do
  def formTimeToJoda(stringTime: Option[String]) = {
    stringTime match {
      case Some(s) => Some(new DateTime())
      case None => None
    }
  }
  def formTimeToJoda(stringTime: String) = {
    new DateTime()
  }
  def jodaTimeToFormTime(jodaTime: Option[DateTime]): Option[String] = {
    jodaTime match {
      case Some(t) => Some(t.toString)
      case None => None
    }
  }
  def jodaTimeToFormTime(jodaTime: DateTime): String = {
    jodaTime.toString
  }




  val incidentForm = Form(
    mapping (
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "incident_type" -> nonEmptyText,
      "status" -> nonEmptyText,
      "next_update_at" -> optional(nonEmptyText),
      "issue_type_id" -> optional(number),
      "issue_id" -> optional(nonEmptyText),
      "primary_responder" -> number,
      "response_team" -> optional(number),
      "user_id" -> number
    )(IncidentFormTemp.apply)(IncidentFormTemp.unapply).verifying(
      "Must include Issue Type Id",
      i => if (!i.issue_type_id.isEmpty && i.issue_id.isEmpty){
        false
      }else{
        true
      }
    )
  )
  
  def addIncident = Action {
    implicit request =>
    Ok(views.html.incidentCreationForm(incidentForm))

  }
  
  def submitIncidentForm = Action {
    implicit request =>
    incidentForm.bindFromRequest.fold(
      formWithErrors =>{
          Ok(views.html.incidentCreationForm(formWithErrors))
          },
        value => {
          //TODO
          Ok("SUBMIITED")
      }
    )
  }









}
