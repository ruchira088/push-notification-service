package utils

import exceptions.{EmptyOptionException, FailedPredicateException}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class FutureO[A](future: Future[Option[A]])
{
  futureO =>

  def flatMap[B](f: A => FutureO[B])(implicit executionContext: ExecutionContext): FutureO[B] =
    FutureO {
      future.flatMap {
        case Some(value) => f(value).future
        case None => Future.successful(None)
      }
    }

  def map[B](f: A => B)(implicit executionContext: ExecutionContext): FutureO[B] = FutureO { future.map(_ map f) }

  def withFilter(predicate: A => Boolean)(implicit executionContext: ExecutionContext): FutureO[A] =
    flatMap(value =>
      if (predicate(value))
        futureO
      else
        FutureO(Future.failed(FailedPredicateException))
    )

  def flatten(exception: => Exception = EmptyOptionException)(implicit executionContext: ExecutionContext): Future[A] = future.flatMap {
    case Some(value) => Future.successful(value)
    case None => Future.failed(exception)
  }
}

object FutureO
{
  implicit def fromTry[A](tryValue: Try[A])(implicit executionContext: ExecutionContext): FutureO[A] =
    fromFuture(Future.fromTry(tryValue))

  implicit def fromFuture[A](future: Future[A])(implicit executionContext: ExecutionContext): FutureO[A] =
    FutureO { future.map(Some(_)) }
}
