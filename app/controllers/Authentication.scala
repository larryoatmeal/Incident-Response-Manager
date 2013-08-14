package controllers
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._

case class LoginClass(id: Int)


object Authentication extends Controller{

  def loginScreen = Action{
    implicit request =>
    Ok(views.html.login(loginForm))
  }

  def loginSubmit = Action{
    implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors =>{
        Ok(views.html.login(formWithErrors))
      },
      value => {
        Redirect(routes.IncidentBrowser.getIncidents()).withSession(
          "id" -> value.id.toString
        )
      }
    )
  }

  val loginForm = Form(
    mapping(
      "id" -> number
    )(LoginClass.apply)(LoginClass.unapply)
  )

  def logout = Action{
    implicit request =>
    Redirect(routes.IncidentBrowser.getIncidents()).withNewSession

  }
}





//This trait is not part of the API
//Just uses Security.Authenticated
trait Secured {

  //Get connected user's email
  private def id(request: RequestHeader) = {
    request.session.get("id")
  }

  //Redirect to login if unauthorized
  private def onUnauthorized(request: RequestHeader) = 
    Results.Redirect(routes.Authentication.loginScreen)

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = 
    Security.Authenticated(id, onUnauthorized){
      user => Action(request => f(user)(request))
  }

}