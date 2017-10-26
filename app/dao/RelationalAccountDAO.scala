package dao

import java.sql.Date
import javax.inject.{Inject, Singleton}

import models.Account
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RelationalAccountDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                    (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with AccountDAO
{
  import profile.api._

  private class Accounts(tag: Tag) extends Table[Account](tag, RelationalAccountDAO.TABLE_NAME)
  {
    implicit val yodaDateTimeColumnType = MappedColumnType.base[DateTime, Date](
      yodaDateTime => new Date(yodaDateTime.getMillis),
      date => new DateTime(date)
    )

    def id = column[String]("id")
    def createdAt = column[DateTime]("created_at")
    def airtableId = column[String]("airtable_id")
    def deviceToken = column[String]("device_token")
    def suburb = column[String]("suburb")
    def state = column[String]("state")
    def mobileNumber = column[String]("mobile_number")
    def email = column[String]("email")

    def pk = primaryKey("pk", id)

    override def * : ProvenShape[Account] =
      (id, createdAt, airtableId, deviceToken, suburb, state, mobileNumber, email) <>
        ((Account.apply _).tupled, Account.unapply)
  }

  private val accountTable = TableQuery[Accounts]

  override def insert(account: Account): Future[Account] = db.run(accountTable += account).map(_ => account)

  override def findByState(state: String): Future[List[Account]] =
    db.run(accountTable.filter(_.state === state).result).map(_.toList)
}

object RelationalAccountDAO
{
  val TABLE_NAME = "account"
}
