package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger


//For informational messages
case class Message(text: String, category: String)




object Helper{

  //For informational messages
  val Info = "INFO"
  val Error = "ERROR"
  val Warning = "WARNING"
  val Success = "SUCCESS"


  def log(a: Any){
    Logger.debug(a.toString)
  }

  // def checkForUnique[T](table: String, column: String, value: T, err: String)= 
  //    DB.withConnection{
  //   implicit connection =>


  //   implicit def rowGeneric: Column[T] = Column.nonNull { (value, meta) =>
  //     val MetaDataItem(qualified, nullable, clazz) = meta
  //     value match {
  //       case t: T => {
  //         Right(t)
  //       }
  //       case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass + " to Generic for column " + qualified))
  //     }
  //   }
    



  //   SQL(s"""
  //     SELECT COUNT(*) FROM teams
  //     WHERE $column = {value}
  //     """).on(
  //       "value" -> value
  //     )().map(
  //       row => row[T]("COUNT(*)")
  //     ).head match {
  //       case 0 => None 
  //       case _ => Some(err) //Error message. Already exists!
  //     }


  // }


  //Add spaces between sql lines
  def sqlFormat(args: String*) = {
    args.foldLeft(""){
      (acc, arg) => acc + " " + arg + " "
    }
  }


  def getSingle[T](id: Int, table: String, idColName: String, parser: RowParser[T]) = DB.withConnection{
    implicit connection =>
      
    try {
      val sql = SQL(s"""SELECT * FROM $table WHERE $idColName = $id""").on(
        //"table" -> table, //THIS IS NOT WORKING!! WHY????
        //"idColName" -> idColName,
        //"id" -> id 
      )
      val objectList = sql.as(parser *)
      //log(objectList)
      //If list is empty, send over none.
      objectList.length match{
        case 0 => None
        case _ => Some(objectList.head)
      }

    }
    catch {
      case e => {
        Logger.error(e.toString)
        None
      }
    }
  }

  def getAll[T](table: String, parser: RowParser[T]) = DB.withConnection{
    implicit connection =>

    try {
      SQL(s"""SELECT * FROM $table""").as(parser *)
    }
    catch {
      case e => {
        Logger.error(e.toString)
        List()
      }
    }
  }

  def getAllNotDeleted[T](table: String, parser: RowParser[T], delCol: String) = DB.withConnection{
    implicit connection =>

    try {
      SQL(s"""SELECT * FROM $table WHERE $delCol = false""").as(parser *)
    }
    catch {
      case e => {
        Logger.error(e.toString)
        List()
      }
    }


  }



  def getAllSort[T](table: String, sort: String, desc: Boolean, parser: RowParser[T]) = DB.withConnection{
    implicit connection =>
    val direction = if(desc){"DESC"}else{"ASC"}

    try {
      SQL(s"""SELECT * FROM $table ORDER BY $sort $direction""").as(parser *)
    }
    catch {
      case e => {
        Logger.error(e.toString)
        List()
      }
    }
  }

  def delete(table: String, col: String,  id: Int) = DB.withConnection{
    implicit conenction =>
    SQL(s"""DELETE FROM $table WHERE $col = $id""").executeUpdate() == 1
  }

  def softDelete(table: String, deleteCol: String, idCol: String, id: Int) = DB.withConnection{
    implicit connection =>

    SQL(s"""UPDATE $table
          SET $deleteCol = true
          WHERE $idCol = {id}""").on("id" -> id).executeUpdate() == 1
  }

  def softRevive(table: String, deleteCol: String, idCol: String, id: Int) = DB.withConnection{
    implicit connection =>

    SQL(s"""UPDATE $table
          SET $deleteCol = false
          WHERE $idCol = {id}""").on("id" -> id).executeUpdate() == 1
  }

  def checkExistence(table: String, column: String, value: String) = DB.withConnection{
    implicit connection =>

    SQL(s"""
      SELECT COUNT(*)
      FROM $table
      WHERE $column = "$value"
      """
    )().map(
      row => {
        row[Long]("COUNT(*)")   
      }
    ).head > 0

  }

  //For multiple subscriptions in forms
  def extractRepeatedFormIndex(raw: String) = {
    import scala.util.matching.Regex
    //Find number inbetween brackets
    val indexPattern = new Regex("""(?<=\[)[0123456789]+(?=\])""")
    val index = indexPattern findFirstIn raw
    //Logger.debug(indexPattern.toString)

    index match {
      case Some(i) => i
      case None => 0
    }
  } 

  def gravatar(email: String, size: Int = 30) = {
    import java.security.MessageDigest
    //Lower case, no spaces
    val emailNormalized = email.toLowerCase.filter(_!=' ')
    val bytesOfMessage = emailNormalized.getBytes("UTF-8")
    val hash = MessageDigest.getInstance("MD5").digest(bytesOfMessage).map("%02x".format(_)).mkString 

    "http://www.gravatar.com/avatar/" + hash + "?d=retro&s=" + size
  }

  def cutoff(text: String, cutoffNumber: Int) = {
    if (text.length > cutoffNumber){
      text.substring(0, cutoffNumber) + "..."
    }else{
      text
    }
  }






}