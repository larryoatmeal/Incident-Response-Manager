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

  def addTeam(team: TeamM) = DB.withConnection{
    implicit connection =>

    try {
      SQL("""INSERT INTO teams VALUES ({id},{name},{deleted})""").on(
        "id" -> team.id,
        "name" -> team.name,
        "deleted" -> team.deleted
      ).executeUpdate() match {
        case 1 => None 
        case _ => Some("Not added")
      }
    }
    catch {
      case e => {
        Logger.error(e.toString)
        Some(e.toString)
      }
    }
  }

  def deleteTeam(id: Int) = Helper.softDelete("teams", "deleted", "id", id)

  def getResponseTeamIncidents(team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL(
      """
      SELECT * FROM incidents WHERE respond_team_id = {team_id}
      """
    ).on("team_id" -> team_id).as(IncidentM.incidentParser *)
  }
  


}