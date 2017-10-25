package utils

import exceptions.FormValidationException
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.Request
import utils.ScalaUtils.toTry
import ScalaUtils._

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object JsonUtils
{
  def deserialize[A](implicit request: Request[JsValue], reads: Reads[A]): Try[A] = deserialize[A](request.body)

  def deserialize[A](jsValue: JsValue)(implicit reads: Reads[A]): Try[A] =
    jsValue.validate[A].fold(
      formErrors => Failure(FormValidationException(formErrors)),
      Success(_)
    )

  def parseIsoDate(isoDate: String): Try[DateTime] = try {
    Success(DateTime.parse(isoDate))
  } catch {
    case NonFatal(throwable) => Failure(throwable)
  }

  implicit val dateTimeFormat: Format[DateTime] = new Format[DateTime]
  {
    override def writes(dateTime: DateTime): JsValue = JsString(dateTime.toString)

    override def reads(json: JsValue): JsResult[DateTime] = for {
      dateTimeString <- toTry(json.asOpt[String])
      dateTime <- parseIsoDate(dateTimeString)
    } yield dateTime
  }
}
