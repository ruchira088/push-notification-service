package services.airtable

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariableNames}
import play.api.libs.ws.WSClient
import services.airtable.model.AirtableQuote
import utils.{ConfigUtils, FutureO, JsonUtils}
import utils.FutureO._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class AirtableServiceImpl @Inject()(wsClient: WSClient)
                                   (implicit executionContext: ExecutionContext) extends AirtableService
{
  def getAirtableServiceUrl(): Try[String] =
    ConfigUtils.getEnvValueAsTry(EnvVariableNames.AIRTABLE_SERVICE_URL)

  override def findQuoteById(quoteId: String): FutureO[AirtableQuote] = for
    {
      airtableServiceUrl <- FutureO.fromTry(getAirtableServiceUrl())

      response <- wsClient.url(s"$airtableServiceUrl/${ConfigValues.getQuoteUrlPath(quoteId)}").get()

      airtableQuote <-  Future.fromTry(JsonUtils.deserialize[AirtableQuote](response.json))
    }
    yield airtableQuote
}
