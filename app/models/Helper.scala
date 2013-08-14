package models

import anorm._
import anorm.RowParser
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger





object Helper{

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

  




}