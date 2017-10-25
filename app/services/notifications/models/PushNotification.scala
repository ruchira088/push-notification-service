package services.notifications.models

import play.api.libs.json.{Json, OFormat}

case class PushNotification(
      title: String,
      body: String,
      pushNotificationData: PushNotificationData
)

object PushNotification
{
  implicit val oFormat: OFormat[PushNotification] = Json.format[PushNotification]
}