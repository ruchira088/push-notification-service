package services.notifications

import java.io.File
import javax.inject.{Inject, Singleton}

import com.turo.pushy.apns.util.{ApnsPayloadBuilder, SimpleApnsPushNotification}
import com.turo.pushy.apns.{ApnsClient, ApnsClientBuilder, PushNotificationResponse}
import constants.ConfigValues
import constants.EnvVariableNames._
import exceptions.RejectedApnsNotificationException
import io.netty.util
import org.apache.commons.lang3.StringUtils
import services.notifications.models.PushNotification
import utils.ConfigUtils._
import utils.FileUtils

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

@Singleton
class ApplePushNotificationService @Inject()(apnsClient: ApnsClient)(implicit executionContext: ExecutionContext)
  extends PushNotificationService
{
  override def send(notification: PushNotification)(deviceToken: String): Future[Unit] =
  {
    val initialPayload = new ApnsPayloadBuilder {
      setAlertTitle(notification.title)
      setAlertBody(notification.body)
    }

    val payload: ApnsPayloadBuilder = notification.pushNotificationData.toMap
      .foldLeft(initialPayload) {
        case (payload, (key, value)) => payload.addCustomProperty(key, value)
      }

    val pushNotification = new SimpleApnsPushNotification(
      deviceToken,
      ConfigValues.APP_ID,
      payload.buildWithDefaultMaximumLength()
    )

    val promise = Promise[PushNotificationResponse[SimpleApnsPushNotification]]()

    apnsClient.sendNotification(pushNotification).addListener(
      (future: util.concurrent.Future[PushNotificationResponse[SimpleApnsPushNotification]]) => {
        val result = future.get()

        if (result.isAccepted) {
          promise.success(result)
        } else {
          promise.failure(RejectedApnsNotificationException(result))
        }
      }
    )

    promise.future.map(_ => ())
  }

}

object ApplePushNotificationService
{
  type ApnsCertificate = File

  def getApnsCertificate(): Try[ApnsCertificate] = for
    {
    apnsCertificateFilePath <- getEnvValueAsTry(APNS_CERTIFICATE_PATH)
    certificateFile <- FileUtils.getFile(apnsCertificateFilePath)
  }
    yield certificateFile

  def buildApnsClient(apnsCertificate: ApnsCertificate, apnsServer: String): ApnsClient =
    new ApnsClientBuilder()
      .setClientCredentials(apnsCertificate, StringUtils.EMPTY)
      .setApnsServer(apnsServer)
      .build()
}