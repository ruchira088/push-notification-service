package controllers

import javax.inject.{Inject, Singleton}

import controllers.request.body.CreateAccount
import dao.AccountDAO
import models.Account
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents, PlayBodyParsers, Request}
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountController @Inject()(
      parser: PlayBodyParsers,
      accountDAO: AccountDAO,
      controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents)
{
  def create() = Action.async(parser.json) {
    implicit request: Request[JsValue] => for
      {
        createAccount <- Future.fromTry(deserialize[CreateAccount])
        account <- accountDAO.insert(Account.fromCreateAccount(createAccount))
      }
      yield Ok(account.toJson)
  }

}
