package utils

import java.util.concurrent

import exceptions.EmptyOptionException
import play.api.libs.json.{JsError, JsResult, JsSuccess}

import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  implicit def fromTry[A](tryValue: Try[A]): JsResult[A] = tryValue match {
    case Success(value) => JsSuccess(value)
    case Failure(throwable) => JsError(throwable.getMessage)
  }

  def toOption[A](value: A): Option[A] = value match {
    case null => None
    case _ => Some(value)
  }

  def toTry[A](option: Option[A], exception: => Exception = EmptyOptionException): Try[A] =
    option.fold[Try[A]](Failure(exception))(Success(_))

  def convert[A, B](f: A => B)(value: A): Try[B] =
    try {
      Success(f(value))
    } catch {
      case NonFatal(throwable) => Failure(throwable)
    }

  // TODO Implement the more efficient Java Future to Scala Future conversion
  def simpleConversionToScalaFuture[A]
  (javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
    Future {
      blocking {
        javaFuture.get()
      }
    }

  def predicate(boolean: Boolean, exception: => Exception, onFail: => Unit = {}): Try[Unit] =
    if (boolean)
      Success(())
    else
      Failure(exception)
}