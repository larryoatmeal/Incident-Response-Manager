package models

case class UserTeamMap(user_id: Int, team_id: Int)


object UserTeamMap {
  import anorm._
  import anorm.RowParser
  import anorm.SqlParser._
  import play.api.Play.current
  import play.api.db.DB
  import play.api.Logger


  val userTeamMapParser: RowParser[UserTeamMap] = {
    import anorm.~
    get[Int]("user_id") ~
    get[Int]("team_id") map {
      case user_id ~ team_id =>
        UserTeamMap(
          user_id,
          team_id
        )
    } 
  }
  
  def getTeams(user_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("SELECT teams.* FROM teams JOIN user_team_map ON teams.id = user_team_map.team_id WHERE user_team_map.user_id = {user_id} ORDER BY teams.name ").on(
      "user_id" -> user_id
    ).as(TeamM.teamParser *)
  }

  def getUsers(team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("SELECT users.* FROM users JOIN user_team_map ON users.id = user_team_map.user_id WHERE user_team_map.team_id = {team_id} ORDER BY users.first_name").on(
      "team_id" -> team_id
    ).as(UserM.userParser *)
  }

  def deleteUser(user_id: Int) = Helper.delete("user_team_map","user_id",user_id)
  def deleteTeam(team_id: Int) = Helper.delete("user_team_map","team_id",team_id)
  def deleteSingleMap(user_id: Int, team_id: Int) = DB.withConnection{
    implicit connection =>
    SQL("DELETE FROM user_team_map WHERE user_id = {user_id} AND team_id = {team_id}").on(
      "user_id" -> user_id,
      "team_id" -> team_id
    ).executeUpdate() == 1
  }

  def addUserTeam(user_id: Int, team_id: Int) = DB.withConnection{//Return if succesful or not
    implicit request =>
    //Check if already exists
    // val exists = SQL("SELECT COUNT(*) FROM user_team_map WHERE user_id = {user_id} AND team_id = {team_id}").on(
    //   "user_id" -> user_id,
    //   "team_id" -> team_id
    // )().map(
    //   row => row[Long]("COUNT(*)")
    // ).head

    // exists match {
    //   case 0 => {
    //     //Add entry
    //     SQL("INSERT INTO user_team_map VALUES ({user_id},{team_id})").on(
    //       "user_id" -> user_id,
    //       "team_id" -> team_id
    //     ).executeUpdate() == 1
    //   }
    //   case _ => {
    //     //Duplicate
    //     false
    //   }
    // }
    try{
      SQL("INSERT INTO user_team_map VALUES ({user_id},{team_id})").on(
            "user_id" -> user_id,
            "team_id" -> team_id
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





}