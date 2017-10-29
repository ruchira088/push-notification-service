package utils

import java.nio.file.Paths

import exceptions.{ConfigValueParseException, UndefinedEnvValueException}
import org.apache.commons.lang3.StringUtils
import utils.FileUtils.readFile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object ConfigUtils
{
  case class KeyValuePair[A](key: String, value: A)

  def getEnvValue(name: String): Option[String] = ScalaUtils.toOption(System getenv name)

  def getEnvValueAsTry(name: String): Try[String] =
    ScalaUtils.toTry(getEnvValue(name), UndefinedEnvValueException(name))

  def getEnvValueOrDefault(name: String, defaultValue: => String): String =
    getEnvValue(name).getOrElse(defaultValue)

  def getApplicationInfo()(implicit executionContext: ExecutionContext): Future[List[KeyValuePair[String]]] =
  {
    val KEY_VALUE_SEPARATOR = ":="
    val BUILD_PROPS_KEY_VALUE_SEPARATOR = "="

    def parseBuildSbtLine(line: String): Try[KeyValuePair[String]] =
      line.replaceAll(",|\"", "").split(KEY_VALUE_SEPARATOR).toList match
      {
        case key :: value :: Nil => Success(KeyValuePair(key.trim, value.trim))
        case value => Failure(ConfigValueParseException(value.mkString))
      }

    for {
      buildSbt <- readFile(Paths.get("build.sbt"))
      buildProps <- readFile(Paths.get("project/build.properties"))

      buildSbtKeyValuePairs = buildSbt.split(StringUtils.LF)
        .foldLeft(List.empty[KeyValuePair[String]]) {
          case (keyValuePairs, line) => parseBuildSbtLine(line) match {
            case Success(keyValue) => keyValuePairs :+ keyValue
            case _ => keyValuePairs
          }
        }

      sbtVersion = buildProps.split(BUILD_PROPS_KEY_VALUE_SEPARATOR).toList.tail.head
    }
      yield buildSbtKeyValuePairs :+ KeyValuePair("sbtVersion", sbtVersion)
  }
}
