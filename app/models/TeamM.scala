package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

case class TeamM(
  id: Pk[Int],
  name: String,
  deleted: Boolean
)


object TeamM {
  
  val dummyTeam = TeamM(anorm.NotAssigned, "Purple", false)

  val teamParser: RowParser[TeamM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("name") ~ 
    get[Boolean]("deleted") map {
      case id ~ name ~ deleted=>
        TeamM(
          id,
          name,
          deleted
        )
    } 
  }
  
  def getTeam(id: Int) = Helper.getSingle[TeamM](id, "teams", "id", teamParser)
  def getTeams = Helper.getAllSort[TeamM]("teams", "name", false, teamParser)

  def addTeam(team: TeamM): Message = DB.withConnection{
    implicit connection =>

    //if team already exists, just make deleted flag false 
    try {
      SQL("""INSERT INTO teams VALUES ({id},{name},{deleted})
        """).on(
        "id" -> team.id,
        "name" -> team.name,
        "deleted" -> team.deleted
      ).executeUpdate() match {
        case 1 => Message("Successfully added team", Helper.Success)
        case _ => Message("Not added", Helper.Error)
      }
    }
    catch {
      case e:com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException => {
        SQL("""UPDATE teams
               SET deleted = false 
               WHERE name = {name}
               AND deleted = true""").on(
                "name" -> team.name
          ).executeUpdate() match {
            case 0 => Message(s"Team with name '${team.name}' already exists", Helper.Error)
            case 1 => Message("Previous team with same name. Previous team revived", Helper.Warning) 
          }  
      }
      case e: Throwable => {
        Logger.error(e.toString)
        Message(e.toString, Helper.Error)
      }
    }
  }

  def deleteTeam(id: Int) = Helper.softDelete("teams", "deleted", "id", id)
  def reviveTeam(id: Int) = Helper.softRevive("teams", "deleted", "id", id)

  def editTeam(team: TeamM, id: Int) = DB.withConnection{
    implicit connection => {

      try{
        SQL("""
        UPDATE teams
        SET name = {name}
        WHERE id = {id}
        """).on(
          "name" -> team.name,
          "id" -> id
        ).executeUpdate()
        Message("Updated team", Helper.Success)
      }
      catch{
        case e:com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException => {
          Message("Team name already exists", Helper.Error)
        }
        case e: Throwable => {
          Message(e.toString, Helper.Error)
        }
      }
    }
  }


  def getResponseTeamIncidents(team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL(
      """
      SELECT * FROM incidents WHERE respond_team_id = {team_id}
      """
    ).on("team_id" -> team_id).as(IncidentM.incidentParser *)
  }
  
  def getUserEmails(team_id: Int) = DB.withConnection{
    implicit connection => 
    SQL(
      """
      SELECT users.email FROM teams
      JOIN user_team_map ON user_team_map.team_id = teams.id
      JOIN users ON users.id = user_team_map.user_id
      WHERE teams.id = {team_id}
      """
    ).on(
      "team_id" -> team_id
    )().map(
      row => row[String]("email")
    ).toList
  }



}