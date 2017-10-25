package utils

import exceptions.UndefinedEnvValueException

import scala.util.Try

object ConfigUtils
{
  def getEnvValue(name: String): Option[String] = ScalaUtils.toOption(System getenv name)

  def getEnvValueAsTry(name: String): Try[String] =
    ScalaUtils.toTry(getEnvValue(name), UndefinedEnvValueException(name))

  def getEnvValueOrDefault(name: String, defaultValue: => String): String =
    getEnvValue(name).getOrElse(defaultValue)
}
