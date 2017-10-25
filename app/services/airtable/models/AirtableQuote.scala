package services.airtable.model

import play.api.libs.json.{Json, OFormat}

case class AirtableQuote(
      rowId: String,
      leadStatus: String,
      eventDate: String,
      eventTime: String,
      suburb: String,
      state: String,
      mobileNumber: String,
      firstName: String,
      email: String,
      cost: BigDecimal,
      supplierPayment: BigDecimal,
      numberOfPeople: Int
)

object AirtableQuote
{
  implicit val oFormat: OFormat[AirtableQuote] = Json.format[AirtableQuote]
}