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
  email: String
)


object UserM {

  val userParser: RowParser[UserM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("first_name") ~
    get[String]("last_name") ~
    get[String]("email") map {
      case id ~ first_name ~ last_name ~ email =>
        UserM(
          id,
          first_name,
          last_name,
          email
        )
    } 
  }
  def getUser(id: Int) = Helper.getSingle[UserM](id, "users", "id", userParser)
  def getUsers = Helper.getAll[UserM]("users", userParser)

  def addUser(user: UserM) = DB.withConnection{
    implicit connection =>
    try{
      SQL("""INSERT INTO users VALUES ({id},{first_name},{last_name},{email})""").on(
        "id" -> user.id,
        "first_name" -> user.first_name,
        "last_name" -> user.last_name,
        "email" -> user.email
      ).executeUpdate() match {
        case 1 => None
        case _ => Some("Not added")

      }
    }
    catch{
      case e => {
        Logger.error(e.toString)
        Some(e.toString)
      }

    }

  }


}