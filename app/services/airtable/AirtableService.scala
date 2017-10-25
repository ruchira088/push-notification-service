package services.airtable

import services.airtable.model.AirtableQuote
import utils.FutureO

trait AirtableService
{
  def findQuoteById(quoteId: String): FutureO[AirtableQuote]
}
