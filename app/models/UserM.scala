package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger


case class UserM(
  id: Pk[Int],
  first_name: String,
  last_name: String,
  email: String,
  deleted: Boolean
)


object UserM {

  val userParser: RowParser[UserM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("first_name") ~
    get[String]("last_name") ~
    get[String]("email") ~ 
    get[Boolean]("deleted") map {
      case id ~ first_name ~ last_name ~ email ~ deleted =>
        UserM(
          id,
          first_name,
          last_name,
          email,
          deleted
        )
    } 
  }
  def getUser(id: Int) = Helper.getSingle[UserM](id, "users", "id", userParser)
  def getUsers = Helper.getAllSort[UserM]("users", "first_name", false, userParser)
  def getUsersNotDeleted = getUsers.filter(!_.deleted)

  def getUserFullName(id: Int) = getUser(id).map(user => user.first_name + " " + user.last_name)

  def addUser(user: UserM): Message = DB.withConnection{

    implicit connection =>
    try{
      SQL("""INSERT INTO users VALUES ({id},{first_name},{last_name},{email},{deleted})""").on(
        "id" -> user.id,
        "first_name" -> user.first_name,
        "last_name" -> user.last_name,
        "email" -> user.email,
        "deleted" -> user.deleted
      ).executeUpdate() match {
        case 1 => Message("Successfully added user", Helper.Success)
        case _ => Message("Not added", Helper.Error)

      }
    }
    catch{
      //For duplicate emails, just turn deleted flag to true
      case e:com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException => {
        SQL("""UPDATE users
               SET deleted = false 
               WHERE email = {email}
               AND deleted = true""").on(
                "email" -> user.email
          ).executeUpdate() match {
            //If user already exists and has not been deleted, update will affect 0 rows
            case 0 => Message(s"User with email '${user.email}' already exists", Helper.Error)
            //If user exists but has been deleted, update will affect one row
            case 1 => Message("Previous user with same email. Previous user revived", Helper.Warning) 
          }
      }
      case e => {
        Logger.error(e.toString)
        Message(e.toString, Helper.Error)
      }

    }
  }


  def deleteUser(id: Int) = Helper.softDelete("users", "deleted", "id", id)

  def editUser(user: UserM, id: Int) = DB.withConnection{
    implicit connection => {
      try{
        SQL("""
        UPDATE users
        SET first_name = {first_name}, last_name = {last_name}, email = {email}
        WHERE id = {id}
        """).on(
          "first_name" -> user.first_name,
          "last_name" -> user.last_name,
          "email" -> user.email,
          "id" -> id
        ).executeUpdate()

        Message("Edited user", Helper.Success)
      }
      catch{
        case e:com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException =>{
          Message("Email already exists", Helper.Error)
        }
        case e =>{
          Message(e.toString, Helper.Error)
        }

      }
    }
  }


  def getPrimaryResponderIncidents(user_id: Int) = DB.withConnection{
    implicit connection =>
    SQL(
      """
      SELECT * FROM incidents WHERE primary_responder = {user_id}
      """
    ).on("user_id" -> user_id).as(IncidentM.incidentParser *)
  }


  //Get incidents where user is part of the response team
  def getResponseTeamIncidents(user_id: Int) = DB.withConnection{
    implicit connection =>
    SQL(
      """
      SELECT incidents.* FROM incidents
      JOIN user_team_map
      ON incidents.respond_team_id = user_team_map.team_id
      WHERE user_team_map.user_id = {user_id}
      """
    ).on("user_id" -> user_id).as(IncidentM.incidentParser *)
  }

  //Gets all incidents user is subscribed to through their teams
  def getSubscriptions(user_id: Int) = DB.withConnection{
    implicit connection =>
    SQL(
      """
      SELECT DISTINCT incidents.* FROM incidents 
      JOIN incident_subscriptions
      ON incident_subscriptions.incident_id = incidents.id
      JOIN user_team_map
      ON user_team_map.team_id = incident_subscriptions.team_id
      WHERE user_team_map.user_id = {user_id}
      ORDER BY incidents.title
      """
    ).on(
      "user_id" -> user_id
    ).as(IncidentM.incidentParser *)
  }



}