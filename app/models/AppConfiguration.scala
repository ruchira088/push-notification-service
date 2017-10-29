package models

import play.api.libs.json.Json
import utils.ConfigUtils.KeyValuePair

case class AppConfiguration(appConfigValues: List[KeyValuePair[String]])
{
  def toJsonObject = appConfigValues.foldLeft(Json.obj()) {
    case (json, KeyValuePair(key, value)) => json ++ Json.obj(key -> value)
  }
}