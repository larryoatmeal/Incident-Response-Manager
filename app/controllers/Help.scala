package controllers

import play.api._
import play.api.mvc._
import models._


object Help extends Controller {

  def index = Action{
    implicit request =>
    Ok(views.html.help())
  }
}