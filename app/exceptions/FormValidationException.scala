package exceptions

import play.api.libs.json.{JsPath, JsonValidationError}

case class FormValidationException(validationErrors: List[(JsPath, List[JsonValidationError])])
  extends Exception

object FormValidationException
{
  def apply(validationErrors: Seq[(JsPath, Seq[JsonValidationError])]): FormValidationException =
    FormValidationException(validationErrors.toList.map {
      case (jsPath, jsonValidationErrors) => (jsPath, jsonValidationErrors.toList)
    })
}