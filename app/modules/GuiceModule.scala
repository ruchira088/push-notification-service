package modules

import com.google.inject.AbstractModule
import com.turo.pushy.apns.{ApnsClient, ApnsClientBuilder}
import constants.{EnvVariableNames, GeneralConstants}
import dao.{AccountDAO, RelationalAccountDAO}
import exceptions.UndefinedEnvValueException
import services.airtable.{AirtableService, AirtableServiceImpl}
import services.notifications.{ApplePushNotificationService, MockPushNotificationService, PushNotificationService}
import utils.{ConfigUtils, SystemUtils}

import scala.util.Try

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    ConfigUtils.getEnvValue(EnvVariableNames.SCALA_ENV) match
    {
      case Some(GeneralConstants.PRODUCTION_ENV_VALUE) => {
        productionEnvBindings()
      }

      case Some(GeneralConstants.LOCAL_TEST_ENV_VALUE) => {
        testEnvBindings()
      }

      case _ => {
        developmentEnvBindings()
      }
    }
  }

  def bindApnsClient(apnsServer: String): Try[Unit] = {
    for
      {
      apnsCertificate <- ApplePushNotificationService.getApnsCertificate()
      apnsClient = ApplePushNotificationService.buildApnsClient(apnsCertificate, apnsServer)
      _ = bind(classOf[ApnsClient]).toInstance(apnsClient)
    } yield ()
  } recover {
    case exception: UndefinedEnvValueException => {
      SystemUtils.terminate(exception)
    }
  }

  def testEnvBindings() =
  {
    bind(classOf[AccountDAO]).to(classOf[RelationalAccountDAO])
    bind(classOf[AirtableService]).to(classOf[AirtableServiceImpl])
    bind(classOf[PushNotificationService]).to(classOf[MockPushNotificationService])

    println("Test environment bindings have been applied.")
  }

  def integratedEnvBindings() =
  {
    bind(classOf[AccountDAO]).to(classOf[RelationalAccountDAO])
    bind(classOf[PushNotificationService]).to(classOf[ApplePushNotificationService])
    bind(classOf[AirtableService]).to(classOf[AirtableServiceImpl])
  }

  def developmentEnvBindings() =
  {
    integratedEnvBindings()

    bindApnsClient(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)

    println("Development environment bindings have been applied.")
  }

  def productionEnvBindings() =
  {
    integratedEnvBindings()
    bindApnsClient(ApnsClientBuilder.PRODUCTION_APNS_HOST)

    println("Production environment bindings have been applied.")
  }
}
