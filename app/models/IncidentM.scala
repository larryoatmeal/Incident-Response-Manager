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
  //sortOrder: Int = IncidentM.ASCENDING
)

object IncidentM {

  //val NoQuery = "NoQuery" //Fake null value for empty query

  val incidentTypes = Map("internal" -> "Internal","external"->"External") //database value -> display
  val statusTypes = Map("open" -> "Open","closed" -> "Closed")

  //QueryOptions constants
    val WHOLEWORD = 0
    val WILDCARD = 1
    val BEFORE_TIME = 2
    val AFTER_TIME = 3
    val SHORTER = 4
    val LONGER = 5
  //Sort option constants
    val ASCENDING = 0
    val DESCENDING = 1

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

  import collection.immutable.ListMap
  val columnMap = ListMap(//sql name -> display name
    "created_at" -> "Created At",
    "updated_at" -> "Updated At",
    "next_update_at" -> "Next Update",
    "title" -> "Title",
    "description" -> "Description",
    "incident_type"-> "Incident Type",
    "status" -> "Status",
    "issue_type" -> "Issue Type",
    "issue_id" -> "Issue ID",
    "primary_responder" -> "Primary Responder",
    "response_team" -> "Response Team",
    "incident_duration" -> "Incident Duration"
  )
  val timeColumns = Set(
    "updated_at", "created_at", "next_update_at"
  )

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

      require(columnMap.contains(queryCol))//contains refers to keys of map
      require(columnMap.contains(sort))


      //Values
      val start = (page - 1) * IncidentsPerPage
      val length = IncidentsPerPage
      //Build queries like legos
      val selectSQL = """SELECT incidents.* FROM incidents"""
      val limitSQL = s"""LIMIT $start, $length"""


      //Join relevant tables only if necessary
      //We want to search/sort not by the id for some columns, but rather by the data
      //In their associated columns
      // Empty string treated as no query
      val joinSQL = {
        //-1 represents null from javascript
        val queryJoin = (query,queryCol) match {
          case ("", _) => "" //If no query, no need to join anything
          case ("-1", "issue_type") => ""//If user finding null rows, don't need join
          case (_, "issue_type") => "LEFT JOIN issue_types ON incidents.issue_type_id = issue_types.id"
          case (_, "primary_responder") => "JOIN users ON incidents.primary_responder = users.id"
          case (_, "response_team") => "LEFT JOIN teams ON incidents.respond_team_id = teams.id"
          case (_, _) => ""
        }
        //Logger.debug(queryJoin)
        //Logger.debug("QueryCol:" + queryCol)
        //Logger.debug("Sort:" + sort)
        val sortJoin = sort match {
          case `queryCol` if (query != "") => "" //If sort is same as queryCol, join already taken care of
          //Must join incedents with null issue types as well
          case "issue_type" => "LEFT JOIN issue_types ON incidents.issue_type_id = issue_types.id"
          case "primary_responder" => "JOIN users ON incidents.primary_responder = users.id"
          case "response_team" => "LEFT JOIN teams ON incidents.respond_team_id = teams.id"
          case  _ => ""
        }
        //Logger.debug(sortJoin)

        Helper.sqlFormat(queryJoin, sortJoin)

      }

      //Check query options

      val queryFinal = (queryOptions, query) match {
        case (_, "") => "" //query empty, don't need to do anything
        case (WILDCARD, q) => "%" + q + "%"
        //Convert inputted time to MySQL string datetime
        case (BEFORE_TIME, q) => "<=" + "'" + AnormJoda.toTimestamp(AnormJoda.formTimeToJoda(q)).toString + "'"
        case (AFTER_TIME, q) => ">=" + "'" + AnormJoda.toTimestamp(AnormJoda.formTimeToJoda(q)).toString + "'"
        case (SHORTER, q) => "<=" + "'" + AnormJoda.periodToMilliseconds(q) + "'"
        case (LONGER, q) => ">=" + "'" + AnormJoda.periodToMilliseconds(q) + "'"
        case (_, q) => query
      } 

  
      val querySQL = (queryFinal, queryCol) match {
        case ("", _) => ""
        case ("-1", "issue_type") => s"WHERE incidents.issue_type_id IS NULL"//Special case for nulls
        case (q, "issue_type") => s"WHERE incidents.issue_type_id = $q"
        case (q, "primary_responder") => s"WHERE users.last_name LIKE '$q'"
        case (q, "response_team") => s"WHERE teams.name LIKE '$q'"
        case (q, "created_at") => s"WHERE created_at $q" //queryFinal contains <= / >= so don't surround in quotes here
        case (q, "updated_at") => s"WHERE updated_at $q"
        case (q, "next_update_at") => s"WHERE next_update_at $q"
        case (q, "incident_duration") => s"WHERE UNIX_TIMESTAMP(finished_at) - UNIX_TIMESTAMP(created_at)   $q"
        case (q, column) => s"WHERE $column LIKE '$q'"
      }

      val sortSQL = sort match {
        case "issue_type" => "ORDER BY issue_types.name"
        case "primary_responder" => "ORDER BY users.last_name"
        case "response_team" => "ORDER BY teams.name"
        case "incident_duration" => "ORDER BY UNIX_TIMESTAMP(finished_at) - UNIX_TIMESTAMP(created_at)"
        case c if timeColumns.contains(c) => s"ORDER BY $c DESC"
        case c => s"ORDER BY $c"
      } 

      val finalSQL = Helper.sqlFormat(selectSQL, joinSQL, querySQL, sortSQL, limitSQL)
      //Logger.debug(SQL(finalSQL).toString)

      Logger.debug(finalSQL)
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

      case e: Throwable => {
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

    require(incidentTypes.contains(incident.incident_type))
    require(statusTypes.contains(incident.status))

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
        executeInsert() match {
          case Some(pk) => pk.toInt
          case None => -1
        }//returns ID of inserted row

  }
  catch {
      case e: Throwable => {
        Logger.error(e.toString)
        -1
      }
  }

  }

  def update(incident_id: Int, user_id: Int) = DB.withConnection{
    implicit connection =>
    Logger.debug("Updaetd")
    SQL(
      """
      UPDATE incidents 
      SET updated_at = {updated_at},
      updated_by = {updated_by}
      WHERE id = {incident_id}
      """
    ).on(
      "updated_at" -> AnormJoda.toTimestamp(new DateTime()),
      "updated_by" -> user_id,
      "incident_id" -> incident_id
    ).executeUpdate() == 1
  }

  def editIncident(incident: IncidentM) = DB.withConnection{
    implicit connection =>
    try{
      SQL("""
      UPDATE incidents
      SET 
      title = {title},
      description = {description},
      incident_type = {incident_type},
      status = {status},
      next_update_at = {next_update_at},
      issue_type_id = {issue_type_id},
      issue_id = {issue_id},
      primary_responder = {primary_responder},
      respond_team_id = {respond_team_id},
      finished_at = {finished_at},
      updated_at = {updated_at},
      updated_by = {updated_by}
      WHERE id = {id}
      """
      ).on(
        "title" ->incident.title,
        "description" ->incident.description,
        "incident_type" ->incident.incident_type,
        "status" ->incident.status,
        "issue_id" ->incident.issue_id,
        "issue_type_id" ->incident.issue_type_id,
        "respond_team_id" ->incident.respond_team_id,
        "primary_responder" ->incident.primary_responder,
        "finished_at" ->toTimestamp(incident.finished_at),
        "next_update_at" ->toTimestamp(incident.next_update_at),
        "updated_at" ->toTimestamp(incident.updated_at),
        "updated_by" ->incident.updated_by,
        "id" -> incident.id.get
      ).executeUpdate()

      Message("Incident edited", Helper.Success)
    }
    catch{
      case e: Throwable => Message(e.toString, Helper.Error)

    } 
  }

  def getAllEmails(incident_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("""SELECT DISTINCT users.email FROM incidents 
           JOIN incident_subscriptions ON incident_subscriptions.incident_id = incidents.id
           JOIN teams ON incident_subscriptions.team_id = teams.id
           JOIN user_team_map ON user_team_map.team_id = teams.id
           JOIN users ON users.id = user_team_map.user_id
           WHERE incident_id = {incident_id}
      """).on(
        "incident_id" -> incident_id
      )().map(
        row => row[String]("email")
      ).toList
  } 





}