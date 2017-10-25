package models

import controllers.request.body.CreateAccount
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import utils.GeneralUtils
import utils.JsonUtils._

case class Account(
      id: String,
      createdAt: DateTime,
      stylistAirtableId: String,
      deviceToken: String,
      suburb: String,
      state: String,
      mobileNumber: String,
      email: String
) {
  account =>

  def toJson = Json.toJson(account)
}

object Account
{
  implicit val oFormat: OFormat[Account] = Json.format[Account]

  def fromCreateAccount(createAccount: CreateAccount): Account = {
    val CreateAccount(deviceToken, state, suburb, stylistAirtableId, mobileNumber, email) = createAccount

    Account(
      GeneralUtils.randomUuid(), DateTime.now(), stylistAirtableId,
      deviceToken, suburb, state, mobileNumber, email
    )
  }
}