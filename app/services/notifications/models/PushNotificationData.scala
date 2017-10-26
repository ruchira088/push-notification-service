package services.notifications.models

import play.api.libs.json.{Json, OFormat}
import services.airtable.model.AirtableQuote
import utils.JsonUtils._

case class PushNotificationData(
       eventDate: String,
       eventTime: String,
       state: String,
       suburb: String,
       supplierPayment: BigDecimal,
       numberOfPeople: Int
) {
  notificationData =>

  def toMap: Map[String, String] =
    Map(
      "eventDate" -> eventDate,
      "eventTime" -> eventTime,
      "state" -> state,
      "suburb" -> suburb,
      "supplierPayment" -> supplierPayment.toString,
      "numberOfPeople" -> numberOfPeople.toString
    )
//
//  def toMap: Map[String, String] =
//    Json.toJsObject(notificationData).value.toMap
//      .map {
//        case (name, jsValue) => (name, jsValue.as[String])
//      }
}

object PushNotificationData
{
  implicit val oFormat: OFormat[PushNotificationData] = Json.format[PushNotificationData]

  def fromAirtableQuote(airtableQuote: AirtableQuote): PushNotificationData =
    PushNotificationData(
      airtableQuote.eventDate,
      airtableQuote.eventTime,
      airtableQuote.state,
      airtableQuote.suburb,
      airtableQuote.supplierPayment,
      airtableQuote.numberOfPeople
    )
}