package controllers

import javax.inject.{Inject, Singleton}

import controllers.request.body.NewGig
import dao.AccountDAO
import exceptions.GigNotFoundException
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents, PlayBodyParsers, Request}
import services.airtable.AirtableService
import services.notifications.PushNotificationService
import services.notifications.models.{PushNotification, PushNotificationData}
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GigController @Inject()(
           parser: PlayBodyParsers,
           airtableService: AirtableService,
           accountDAO: AccountDAO,
           pushNotificationService: PushNotificationService,
           controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents)
{
  def newGig() = Action.async(parser.json) {
    implicit request: Request[JsValue] => for
    {
      NewGig(gigId) <- Future.fromTry(deserialize[NewGig])
      airtableQuote <- airtableService.findQuoteById(gigId).flatten(GigNotFoundException(gigId))

      accounts <- accountDAO.findByState(airtableQuote.state)
      deviceTokens = accounts.map(_.deviceToken)

      pushNotification = PushNotification(
        "alert title",
        "alert body",
        PushNotificationData.fromAirtableQuote(airtableQuote)
      )

      _ <- pushNotificationService.sendAll(deviceTokens, pushNotification)
    }
    yield Ok(Json.obj(
      "pushNotification" -> Json.toJsObject(pushNotification),
      "count" -> deviceTokens.length
    ))
  }
}
