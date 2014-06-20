  package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

case class IssueTypeM(
  id: Pk[Int],
  slug: String,
  clazz: Option[String],
  name: String
)


object IssueTypeM {
  val dummyIssueType = IssueTypeM(anorm.NotAssigned, "Slugger", Some("Clazzy"), "PagerDuty")

  val issueTypeParser: RowParser[IssueTypeM] = {
    import anorm.~
    get[Pk[Int]]("id") ~
    get[String]("slug") ~
    get[Option[String]]("clazz") ~
    get[String]("name") map {
      case id ~ slug ~ clazz ~ name =>
        IssueTypeM(
          id,
          slug,
          clazz,
          name
        )
    } 
  }


  def getIssueType(id: Int) = Helper.getSingle[IssueTypeM](id, "issue_types", "id", issueTypeParser)
  def getIssueTypes = Helper.getAll[IssueTypeM]("issue_types", issueTypeParser)

  def addIssueType(issueType: IssueTypeM) = DB.withConnection{
    implicit connection =>

    try{
      SQL("""
        INSERT INTO issue_types
        VALUES({id},{slug},{clazz},{name})
        """).on(
        "id" -> issueType.id,
        "slug" -> issueType.slug,
        "clazz" -> issueType.clazz,
        "name" -> issueType.name
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