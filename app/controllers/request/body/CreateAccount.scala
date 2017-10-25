package controllers.request.body

import play.api.libs.json.{Json, OFormat}

case class CreateAccount(
      deviceToken: String,
      state: String,
      suburb: String,
      stylistAirtableId: String,
      mobileNumber: String,
      email: String
)

object CreateAccount
{
  implicit val oFormat: OFormat[CreateAccount] = Json.format[CreateAccount]
}