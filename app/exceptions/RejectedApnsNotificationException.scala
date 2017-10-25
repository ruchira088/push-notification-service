package exceptions

import com.turo.pushy.apns.{ApnsPushNotification, PushNotificationResponse}

case class RejectedApnsNotificationException[T <: ApnsPushNotification](result: PushNotificationResponse[T])
  extends Exception
