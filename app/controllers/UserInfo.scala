package controllers

import play.api._
import play.api.mvc._
import models._


object UserInfo extends Controller {

  def userPage(id: Int) = Action{
    implicit request =>
    Ok(views.html.userView(id))
  }

  def userList = Action{
    implicit request =>
    val users = UserM.getUsers
    Ok(views.html.userList(users))
  }


}