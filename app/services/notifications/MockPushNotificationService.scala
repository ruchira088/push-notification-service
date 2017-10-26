package services.notifications
import javax.inject.Singleton

import services.notifications.models.PushNotification

import scala.concurrent.Future

@Singleton
class MockPushNotificationService() extends PushNotificationService
{
  override def send(pushNotification: PushNotification)(deviceToken: String) =
  {
    println(pushNotification)
    println(s"deviceToken: $deviceToken")

    Future.successful((): Unit)
  }
}
