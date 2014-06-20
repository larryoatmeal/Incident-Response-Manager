package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

import org.joda.time.DateTime
import java.sql.Timestamp
import AnormJoda._ //implicit conversions


case class IncidentUpdateM(
  id: Pk[Int],
  incident_id: Int,
  description: String,
  created_by: Int,
  created_at: DateTime,
  deleted: Boolean
)


object IncidentUpdateM{
    
  
  val dummyUpdate = IncidentUpdateM(anorm.NotAssigned, 1,
    "Update Gamma", 2, new DateTime, false) 

  val incidentUpdateParser: RowParser[IncidentUpdateM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[Int]("incident_id") ~
    get[String]("description") ~
    get[Int]("created_by") ~
    get[DateTime]("created_at") ~
    get[Boolean]("deleted") map {
      case id ~ incident_id ~ description ~
        created_by ~ created_at ~ deleted =>
        IncidentUpdateM(
          id,
          incident_id,
          description,
          created_by,
          created_at,
          deleted
        )
    } 
  }

  def getIncidentUpdates(incident_id: Int): List[IncidentUpdateM] = DB.withConnection {
    implicit connection =>

    SQL("SELECT * FROM incident_updates WHERE incident_id = {incident_id} ORDER BY created_at DESC").on(
      "incident_id" -> incident_id
    ).as(incidentUpdateParser *)
  }

  def addIncidentUpdate(update: IncidentUpdateM) = DB.withConnection{
    implicit connection => 

    try {
      SQL("""INSERT INTO incident_updates VALUES
        (
          {id},
          {incident_id},
          {description},
          {created_by},
          {created_at},
          {deleted}
        )
        """).on(
          "id" -> update.id,
          "incident_id" -> update.incident_id,
          "description" -> update.description,
          "created_by" -> update.created_by,
          "created_at" -> toTimestamp(update.created_at),
          "deleted" -> update.deleted
        ).executeUpdate() match {
          case 1 => None 
          case _ => Some("Not added")
        }
    }
    catch {
      case e: Throwable => {
        Logger.error(e.toString)
        Some(e.toString)
      }
    }
  }
  
  def deleteUpdate(id: Int) = DB.withConnection{
    implicit connection =>

    SQL("""UPDATE incident_updates
           SET deleted = true
           WHERE id = {id}""").on(
           "id" -> id
          ).executeUpdate() == 1
  }
}