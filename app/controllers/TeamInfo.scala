package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object TeamInfo extends Controller {

  def teamPage(id: Int) = Action{
    implicit request =>
    Ok(views.html.teamView(id))
  }  

  def teamList = Action{
    implicit request =>
    val teams = TeamM.getTeams
    Ok(views.html.teamList(teams))
  }

  val teamForm = Form(
    mapping(
      "name" -> nonEmptyText
    )
    ((name) => TeamM(anorm.NotAssigned, name, false))
    (team => Some(team.name))
  )

  def addTeam = Action{
    implicit request =>
    Ok(views.html.teamForm(teamForm))
  }

  def submitTeamForm = Action{
    implicit request =>
    teamForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.teamForm(formWithErrors)),
      value => {
        val result = TeamM.addTeam(value)

        result match {
          case Some(message) => Redirect(routes.TeamInfo.teamList).flashing("message" -> message)
          case None => Redirect(routes.TeamInfo.teamList)
        }

      }
    )
  }

  def deleteTeam(id: Int) = Action{
    implicit request =>
    TeamM.deleteTeam(id)
    UserTeamMap.deleteTeam(id)//Delete map keys
    IncidentSubscriptionsMap.deleteTeamMaps(id)
    Redirect(routes.TeamInfo.teamList)
  }

  def addUserMap(user_id: Int, team_id: Int) = Action{
    implicit request =>
    UserTeamMap.addUserTeam(user_id, team_id)
    Redirect(routes.TeamInfo.teamPage(team_id))

  }

  def deleteUserMap(user_id: Int, team_id: Int) = Action{
    implicit request =>
    UserTeamMap.deleteSingleMap(user_id, team_id)
    Redirect(routes.TeamInfo.teamPage(team_id))
  }

}