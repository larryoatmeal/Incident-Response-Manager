package controllers

import play.api._
import play.api.mvc._
import models._


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
}