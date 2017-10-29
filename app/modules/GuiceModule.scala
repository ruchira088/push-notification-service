package modules

import com.google.inject.AbstractModule
import com.turo.pushy.apns.{ApnsClient, ApnsClientBuilder}
import constants.GeneralConstants._
import constants.{EnvVariableNames, GeneralConstants}
import dao.{AccountDAO, RelationalAccountDAO}
import exceptions.UndefinedEnvValueException
import models.AppConfiguration
import services.airtable.{AirtableService, AirtableServiceImpl}
import services.notifications.{ApplePushNotificationService, MockPushNotificationService, PushNotificationService}
import utils.ConfigUtils.KeyValuePair
import utils.{ConfigUtils, SystemUtils}

import scala.concurrent.Await
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    ConfigUtils.getEnvValue(EnvVariableNames.SCALA_ENV) match
    {
      case Some(PRODUCTION_ENV_VALUE) => {
        productionEnvBindings()
      }

      case Some(LOCAL_TEST_ENV_VALUE) => {
        testEnvBindings()
      }

      case _ => {
        developmentEnvBindings()
      }
    }
  }

  def bindAppConfiguration(environment: String) =
  {
    val appConfigValues = for
      {
      applicationInfo <- ConfigUtils.getApplicationInfo()
      keyValuePairs = applicationInfo :+ KeyValuePair("environment", environment)
    }
      yield AppConfiguration(keyValuePairs)

    bind(classOf[AppConfiguration]).toInstance(Await.result(appConfigValues, 30 seconds))
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
    bindAppConfiguration(LOCAL_TEST_ENV_VALUE)

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
    bindAppConfiguration(DEVELOPMENT_ENV_VALUE)

    println("Development environment bindings have been applied.")
  }

  def productionEnvBindings() =
  {
    integratedEnvBindings()

    bindApnsClient(ApnsClientBuilder.PRODUCTION_APNS_HOST)
    bindAppConfiguration(PRODUCTION_ENV_VALUE)

    println("Production environment bindings have been applied.")
  }
}
