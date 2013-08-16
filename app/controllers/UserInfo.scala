package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints

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

  
  val userForm = Form(
    mapping(
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
      "email" -> nonEmptyText.verifying(Constraints.pattern("""\b[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}\b""".r, error="Not a valid email address"))
    )
    ((first_name, last_name, email) => UserM(anorm.NotAssigned, first_name, last_name, email, false))
    (user => Some(user.first_name, user.last_name, user.email))
  )

  def addUser = Action{
    implicit request =>
    Ok(views.html.userForm(userForm))
  }

  def submitUserForm = Action{
    implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.userForm(formWithErrors)),
      value => {
        val message = UserM.addUser(value)
        Redirect(routes.UserInfo.userList).flashing(
          "message" -> message.text,
          "category" -> message.category
        )
      }
    )
  }

  def deleteUser(id: Int) = Action{
    implicit request =>
    UserM.deleteUser(id)
    UserTeamMap.deleteUser(id)//Delete map keys

    //If deleted user is currently logged in, logout
    if(id == session.get("id").getOrElse("-1").toInt){
      Redirect(routes.Authentication.logout)
    }else{
      Redirect(routes.UserInfo.userList)
    }
  }

  def editUser(id: Int) = Action{
    implicit request =>

    UserM.getUser(id) match {
      case Some(u) => Ok(views.html.userEdit(userForm.fill(u), id))
      case None => Redirect(routes.UserInfo.userPage(id)).flashing(
        "message" -> "User does not exist",
        "category" -> Helper.Error
      )
    }
  }

  def submitUserEditForm(id: Int) = Action{
    implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.userEdit(formWithErrors, id))
      },
      value => {
        val result = UserM.editUser(value, id)
        Redirect(routes.UserInfo.userPage(id)).flashing(
          "message" -> result.text,
          "category" -> result.category
        )
      }
    )
  }


  def addTeamMap(user_id: Int, team_id: Int) = Action{
    implicit request =>
    UserTeamMap.addUserTeam(user_id, team_id)
    Redirect(routes.UserInfo.userPage(user_id))
  }

  def deleteTeamMap(user_id: Int, team_id: Int) = Action{
    implicit request =>
    UserTeamMap.deleteSingleMap(user_id, team_id)
    Redirect(routes.UserInfo.userPage(user_id))
  }




}