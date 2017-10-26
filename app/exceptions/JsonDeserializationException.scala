package exceptions

import play.api.libs.json.{JsPath, JsonValidationError}

case class JsonDeserializationException(validationErrors: List[(JsPath, List[JsonValidationError])])
  extends Exception
{
  override def getMessage = validationErrors.toString()
}

object JsonDeserializationException
{
  def apply(validationErrors: Seq[(JsPath, Seq[JsonValidationError])]): JsonDeserializationException =
    JsonDeserializationException(validationErrors.toList.map {
      case (jsPath, jsonValidationErrors) => (jsPath, jsonValidationErrors.toList)
    })
}