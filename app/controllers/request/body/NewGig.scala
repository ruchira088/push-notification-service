package controllers.request.body

import play.api.libs.json.{Json, OFormat}

case class NewGig(gigId: String)

object NewGig
{
  implicit val oFormat: OFormat[NewGig] = Json.format[NewGig]
}
