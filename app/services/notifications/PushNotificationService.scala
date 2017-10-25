package services.notifications

import constants.ConfigValues.CONCURRENT_PUSH_NOTIFICATIONS
import services.notifications.models.PushNotification

import scala.concurrent.{ExecutionContext, Future}

trait PushNotificationService
{
  def send(pushNotification: PushNotification)(deviceToken: String): Future[Unit]

  def sendAll(deviceTokens: List[String], pushNotification: PushNotification)
             (implicit executionContext: ExecutionContext): Future[Unit] =
  {
    if (deviceTokens.isEmpty)
      Future.successful((): Unit)
    else
      for {
        _ <- Future.sequence(deviceTokens.take(CONCURRENT_PUSH_NOTIFICATIONS).map(send(pushNotification)))

        result <- sendAll(deviceTokens.drop(CONCURRENT_PUSH_NOTIFICATIONS), pushNotification)
      }
      yield result
  }
}
