package dao

import models.Account

import scala.concurrent.Future

trait AccountDAO
{
  def insert(account: Account): Future[Account]

  def findByState(state: String): Future[List[Account]]
}
