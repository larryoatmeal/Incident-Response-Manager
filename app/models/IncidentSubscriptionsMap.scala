package models

case class IncidentSubscriptionsMap(incident_id: Int, team_id: Int)

//Pretty much exact copy of UserTeamMap
object IncidentSubscriptionsMap {
  import anorm._
  import anorm.RowParser
  import anorm.SqlParser._
  import play.api.Play.current
  import play.api.db.DB
  import play.api.Logger


  val incidentSubscriptionParser: RowParser[IncidentSubscriptionsMap] = {
    import anorm.~
    get[Int]("incident_id") ~
    get[Int]("team_id") map {
      case incident_id ~ team_id =>
        IncidentSubscriptionsMap(
          incident_id,
          team_id
        )
    } 
  }
  
  def getTeams(incident_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("SELECT teams.* FROM teams JOIN incident_subscriptions ON teams.id = incident_subscriptions.team_id WHERE incident_subscriptions.incident_id = {incident_id} ORDER BY teams.name").on(
      "incident_id" -> incident_id
    ).as(TeamM.teamParser *)
  }


  def getIncidents(team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("SELECT incidents.* FROM incidents JOIN incident_subscriptions ON incidents.id = incident_subscriptions.incident_id WHERE incident_subscriptions.team_id = {team_id} ").on(
      "team_id" -> team_id
    ).as(IncidentM.incidentParser *)
  }

  def addSubscription(incident_id: Int, team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("INSERT INTO incident_subscriptions VALUES({incident_id},{team_id})").on(
      "incident_id" -> incident_id,
      "team_id" -> team_id
    ).executeUpdate() == 1

  }
  def deleteSubscription(incident_id: Int, team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("DELETE FROM incident_subscriptions WHERE incident_id = {incident_id} AND team_id = {team_id}").on(
      "incident_id" -> incident_id,
      "team_id" -> team_id
    ).executeUpdate() == 1

  }
  def deleteTeamMaps(team_id: Int) = Helper.delete("incident_subscriptions", "team_id", team_id)

  

  // def addSubscription(incident_id: Int, team_id: Int) = DB.withConnection{//Return if succesful or not
  //   implicit request =>
  //   // //Check if already exists
  //   // val exists = SQL("SELECT COUNT(*) FROM incident_subscriptions WHERE incident_id = {incident_id} AND team_id = {team_id}").on(
  //   //   "incident_id" -> incident_id,
  //   //   "team_id" -> team_id
  //   // )().map(
  //   //   row => row[Long]("COUNT(*)")
  //   // ).head

  //   // exists match {
  //   //   case 0 => {
  //   //     //Add entry
  //   //     SQL("INSERT INTO incident_subscriptions VALUES ({incident_id},{team_id})").on(
  //   //       "incident_id" -> incident_id,
  //   //       "team_id" -> team_id
  //   //     ).executeUpdate() == 1
  //   //   }
  //   //   case _ => {
  //   //     //Duplicate
  //   //     false
  //   //   }
  //   // }


  //   try {
  //     SQL("INSERT INTO incident_subscriptions VALUES ({incident_id},{team_id})").on(
  //         "incident_id" -> incident_id,
  //         "team_id" -> team_id
  //       ).executeUpdate() match {
  //       case 1 => None
  //       case _ => Some("Not added")

  //     }
  //   }
  //   catch {
  //     case e => {
  //       Logger.error(e.toString)
  //       Some(e.toString)
  //     }
  //   }
  // }





}