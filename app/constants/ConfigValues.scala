package constants

object ConfigValues
{
  val APP_ID = "com.ruchij.flayrstylist"

  val CONCURRENT_PUSH_NOTIFICATIONS = 20

  def getQuoteUrlPath(quoteId: String): String = s"query/gigs/$quoteId"
}
