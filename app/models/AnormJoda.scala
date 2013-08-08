package models

import anorm._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.Period
import org.joda.time.Duration
import org.joda.time.format.PeriodFormat
import org.joda.time.format.PeriodFormatterBuilder
import java.sql.Timestamp

//Converts java.sql.Timestamp to joda.time.DateTime
//Needed for anorm to parse dates from MySQL

object AnormJoda{
  implicit def rowToDateTime: Column[DateTime] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case t: Timestamp => {
        Right(new DateTime(t.getTime))
      }
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass + " to TimeStamp for column " + qualified))
    }
  }

  def toTimestamp(dateTime: Option[DateTime]) = {
    dateTime match {
      case Some(dt) => Some(new Timestamp(dt.getMillis()))
      case None => None
    }
  }

  def toTimestamp(dateTime: DateTime) = {
    new Timestamp(dateTime.getMillis())
  }

  def formatDate(dateTime: DateTime) = {
    //val year = dateTime.basicDate()

    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val output = fmt.print(dateTime);
    output

  }
  def formatDate(dateTime: Option[DateTime]): String = {
    dateTime match {
      case Some(dt) => formatDate(dt)
      case None => "--"
    }
  }
  def timeDiff(created: DateTime, finish: Option[DateTime]) = {
    finish match {
      case Some(f) => {
        val diff = f.getMillis() - created.getMillis()
        if (diff < 0){
          "Anachronism"
        }
        else{



          val duration = new Duration(diff)
         
          duration.getStandardHours() + " hours"

        }
      } 
      case None => "NA"


    }
  }

}