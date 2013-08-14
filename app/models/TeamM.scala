package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

case class TeamM(
  id: Pk[Int],
  name: String
)


object TeamM {
  
  val dummyTeam = TeamM(anorm.NotAssigned, "Purple")

  val teamParser: RowParser[TeamM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("name") map {
      case id ~ name =>
        TeamM(
          id,
          name
        )
    } 
  }
  
  def getTeam(id: Int) = Helper.getSingle[TeamM](id, "teams", "id", teamParser)
  def getTeams = Helper.getAllSort[TeamM]("teams", "name", false, teamParser)

  def addTeam(team: TeamM) = DB.withConnection{
    implicit connection =>

    try {
      SQL("""INSERT INTO teams VALUES ({id},{name})""").on(
        "id" -> team.id,
        "name" -> team.name
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

}