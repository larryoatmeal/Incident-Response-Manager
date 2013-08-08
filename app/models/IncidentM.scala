package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

import org.joda.time.DateTime
import java.sql.Timestamp
import AnormJoda._


case class IncidentM(
  id: Pk[Int], //primary key. Use anorm.NotAssigned when inserting row
  title: String,
  description: String,
  incident_type: String,
  status: String,
  issue_id: Option[String],
  issue_type_id: Option[Int],
  respond_team_id: Option[Int],
  primary_responder: Int,
  started_at: Option[DateTime],
  finished_at: Option[DateTime],
  next_update_at: Option[DateTime],
  created_at: DateTime,
  updated_at: DateTime,
  created_by: Int,
  updated_by: Option[Int]
)

case class BrowserParams(
  page: Int,
  sort: String,
  query: String,
  queryCol: String,
  queryOptions: Int
)

object IncidentM {

  val NoQuery = "NoQuery"

  val incidentTypes = List("internal","external")
  val statusTypes = List("open","closed")


  val dummyIncident = IncidentM(
    anorm.NotAssigned,
    "Dummy Incident",
    "Dummy problem",
    "internal",
    "closed",
    None,
    None,
    None,
    1,
    None,
    None,
    None,
    new DateTime(),
    new DateTime(),
    1,
    None
  )
  
  val IncidentsPerPage = 50
  val columns = List(
    "updated_at",
    "title",
    "description",
    "incident_type",
    "status",
    "issue_type",
    "issue_id",
    "created_at",
    "next_update_at",
    "primary_responder",
    "response_team",
    "incident_duration"
  )
  val columnNames = List(
    "Updated At",
    "Title",
    "Description",
    "Incident Type",
    "Status",
    "Issue Type",
    "Issue ID",
    "Created At",
    "Next Update At",
    "Primary Responder",
    "Response Team",
    "Incident Duration"
  )
  val columnTuples = columnNames zip columns


  val incidentParser: RowParser[IncidentM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("title") ~
    get[String]("description") ~
    get[String]("incident_type") ~
    get[String]("status") ~
    get[Option[String]]("issue_id") ~
    get[Option[Int]]("issue_type_id") ~
    get[Option[Int]]("respond_team_id") ~
    get[Int]("primary_responder") ~
    get[Option[DateTime]]("started_at") ~
    get[Option[DateTime]]("finished_at") ~
    get[Option[DateTime]]("next_update_at") ~
    get[DateTime]("created_at") ~
    get[DateTime]("updated_at") ~
    get[Int]("created_by") ~
    get[Option[Int]]("updated_by") map {
      case id ~ title ~ description ~ incident_type ~
        status ~ issue_id ~ issue_type_id ~ respond_team_id ~
        primary_responder ~ started_at ~ finished_at ~
        next_update_at ~ created_at ~ updated_at ~
        created_by ~ updated_by =>
        IncidentM(
          id,
          title,
          description,
          incident_type,
          status,
          issue_id,
          issue_type_id,
          respond_team_id,
          primary_responder,
          started_at,
          finished_at,
          next_update_at,
          created_at,
          updated_at,
          created_by,
          updated_by
        )
    } 
  }
  def getOneIncident(id: Int): Option[IncidentM] = 
    Helper.getSingle[IncidentM](id, "incidents", "id", incidentParser)


  def getIncidents(page: Int, 
    sort: String, 
    query: String, 
    queryCol: String,
    queryOptions: Int): Tuple3[List[IncidentM],Long, Option[String]] = DB.withConnection {
    implicit connection =>
    

    try {

      require(columns.contains(queryCol))
      require(columns.contains(sort))

      //QueryOptions constants
      val WHOLEWORD = 0
      val WILDCARD = 1
      val MATCHCASE = 2

      //Values
      val start = (page - 1) * IncidentsPerPage
      val length = IncidentsPerPage
      //Build queries like legos
      val selectSQL = """SELECT incidents.* FROM incidents"""
      val limitSQL = s"""LIMIT $start, $length"""


      //Join relevant tables only if necessary
      //We want to search/sort not by the id for some columns, but rather by the data
      //In their associated columns
      val joinSQL = {

        val queryJoin = (query,queryCol) match {
          case (NoQuery, _) => "" //If no query, no need to join anything
          case (_, "issue_type") => "JOIN issue_types ON incidents.issue_type_id = issue_types.id"
          case (_, "primary_responder") => "JOIN users ON incidents.primary_responder = users.id"
          case (_, "response_team") => "JOIN teams ON incidents.respond_team_id = teams.id"
          case (_, _) => ""
        }
        //Logger.debug(queryJoin)
        //Logger.debug("QueryCol:" + queryCol)
        //Logger.debug("Sort:" + sort)
        val sortJoin = sort match {
          case `queryCol` if (query != NoQuery) => "" //If sort is same as queryCol, join already taken care of
          case "issue_type" => "JOIN issue_types ON incidents.issue_type_id = issue_types.id"
          case "primary_responder" => "JOIN users ON incidents.primary_responder = users.id"
          case "response_team" => "JOIN teams ON incidents.respond_team_id = teams.id"
          case  _ => ""
        }
        //Logger.debug(sortJoin)

        Helper.sqlFormat(queryJoin, sortJoin)

      }

      
      val queryFinal = (queryOptions, query) match {
        case (_, NoQuery) => NoQuery
        case (WILDCARD, q) => "%" + q + "%"
        case (_, q) => query
      }

  
      val querySQL = (queryFinal, queryCol) match {
        case (NoQuery, _) => ""
        case (q, "issue_type") => s"WHERE issue_types.name LIKE '$q'"
        case (q, "primary_responder") => s"WHERE users.last_name LIKE '$q'"
        case (q, "response_team") => s"WHERE teams.name LIKE '$q'"
        case (q, "all") => s"WHERE * LIKE '$q'"
        case (q, "incident_duration") => s"WHERE UNIX_TIMESTAMP(finished_at) - UNIX_TIMESTAMP(created_at) LIKE '$q'"
        case (q, column) => s"WHERE $column LIKE '$q'"
      }

      val sortSQL = sort match {
        case "issue_type" => "ORDER BY issue_types.name"
        case "primary_responder" => "ORDER BY users.last_name"
        case "response_team" => "ORDER BY teams.name"
        case "incident_duration" => "ORDER BY UNIX_TIMESTAMP(finished_at) - UNIX_TIMESTAMP(created_at)"
        case s => s"ORDER BY $s"
      } 

      val finalSQL = Helper.sqlFormat(selectSQL, joinSQL, querySQL, sortSQL, limitSQL)
      //Logger.debug(SQL(finalSQL).toString)


      val incidentList = SQL(finalSQL).as(incidentParser *)

      //Not very efficient
      val numberOfIncidents = 
        SQL(Helper.sqlFormat("SELECT COUNT(*) FROM incidents", joinSQL, querySQL))().map(
          row => row[Long]("COUNT(*)")
        ).head

      require(page <= pageRangeMax(numberOfIncidents))

      //Send over list, number of incidents, and error message
      Tuple3(incidentList, numberOfIncidents, None)
    }
    catch{

      case e => {
        Logger.error(e.toString)
        Tuple3(List(), 0, Some(e.toString))

      }
    }
  }

  def pageRangeMax(entries: Long) = {
    entries match {
      case 0 => 1 //If 0 entries, still allow one page
      case n => (entries-1)/IncidentsPerPage + 1
      //If 50 entries, only show 1 page, not 2


    }

    

  } 


  def addIncident(incident: IncidentM) = DB.withConnection{
    implicit connection =>

    try {
      SQL("""
        INSERT INTO incidents
        VALUES ({id},
            {title},
            {description},
            {incident_type},
            {status},
            {issue_id},
            {issue_type_id},
            {respond_team_id},
            {primary_responder},
            {started_at},
            {finished_at},
            {next_update_at},
            {created_at},
            {updated_at},
            {created_by},
            {updated_by})
        """).on(
            "id" ->incident.id,
            "title" ->incident.title,
            "description" ->incident.description,
            "incident_type" ->incident.incident_type,
            "status" ->incident.status,
            "issue_id" ->incident.issue_id,
            "issue_type_id" ->incident.issue_type_id,
            "respond_team_id" ->incident.respond_team_id,
            "primary_responder" ->incident.primary_responder,
            "started_at" ->toTimestamp(incident.started_at),
            "finished_at" ->toTimestamp(incident.finished_at),
            "next_update_at" ->toTimestamp(incident.next_update_at),
            "created_at" ->toTimestamp(incident.created_at),
            "updated_at" ->toTimestamp(incident.updated_at),
            "created_by" ->incident.created_by,
            "updated_by" ->incident.updated_by).
        executeUpdate() match {
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