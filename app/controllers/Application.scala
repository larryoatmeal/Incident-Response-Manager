package controllers

import play.api._
import play.api.mvc._
import models._


object Application extends Controller {

  def log(a: Any){
    Logger.debug(a.toString)
  }

  def home =  Action{
    implicit request =>
    Redirect(routes.IncidentBrowser.getIncidents())
  }


  def debug = Action{
    implicit request =>
    // log(IncidentM.getOneIncident(1))
    // log(IncidentUpdateM.getIncidentUpdates(1))
    // log(UserM.getUser(1))
    // log(UserTeamMap.getTeams(1))
    // log(UserTeamMap.getTeams(2))
    // log(UserTeamMap.getTeams(3))
    // log(UserTeamMap.getTeams(4))
    // log(UserTeamMap.getTeams(90))
    // log(UserTeamMap.getUsers(1))
    // log(IncidentM.addIncident(IncidentM.dummyIncident))
    // log(IncidentUpdateM.addIncidentUpdate(IncidentUpdateM.dummyUpdate))
    // log(IncidentSubscriptionsMap.getTeams(1))
    // log(IncidentSubscriptionsMap.getTeams(2))
    // log(IncidentSubscriptionsMap.getIncidents(2))
    // log(TeamM.addTeam(TeamM.dummyTeam))
    // log(IncidentM.getIncidents(1, "primary_responder","Larry","primary_responder"))
    //log(IncidentM.getIncidents(1, "primary_responder","Red","response_team"))
    // log(IncidentM.getIncidents(1, "primary_responder","JIRA","issue_type"))
    // log(IncidentM.getIncidents(1, "primary_responder","1","primary_responder"))
    //log(IssueTypeM.addIssueType(IssueTypeM.dummyIssueType))
    // log(IssueTypeM.getIssueType(1))
    log(AnormJoda.periodToMilliseconds("12:45:45"))
    Emailer.send(IncidentCreatedMail(2))
    //Emailer.sendEmail()
    //Emailer.send(1)
      
    Ok(views.html.debug())
  }

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        IncidentBrowser.getIncidents,
        IncidentUpdate.markDeleted,
        TeamInfo.addUserMap,
        TeamInfo.deleteUserMap,
        UserInfo.addTeamMap,
        UserInfo.deleteTeamMap
      )
    ).as("text/javascript") 
  }






  
}