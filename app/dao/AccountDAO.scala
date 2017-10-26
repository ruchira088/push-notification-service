package dao

import models.Account
import utils.FutureO

import scala.concurrent.Future

trait AccountDAO
{
  def insert(account: Account): Future[Account]

  def findByState(state: String): Future[List[Account]]

  def getByDeviceToken(deviceToken: String): FutureO[Account]

  def tokenExists(deviceToken: String): Future[Boolean]
}
