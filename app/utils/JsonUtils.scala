package utils

import exceptions.FormValidationException
import play.api.libs.json._
import play.api.mvc.Request

import scala.util.{Failure, Success, Try}

object JsonUtils
{
  def deserialize[A](implicit request: Request[JsValue], reads: Reads[A]): Try[A] = deserialize[A](request.body)

  def deserialize[A](jsValue: JsValue)(implicit reads: Reads[A]): Try[A] =
    jsValue.validate[A].fold(
      formErrors => Failure(FormValidationException(formErrors)),
      Success(_)
    )
}
