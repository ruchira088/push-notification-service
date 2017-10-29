package controllers

import javax.inject.{Inject, Singleton}

import models.AppConfiguration
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class InfoController @Inject()(appConfiguration: AppConfiguration, controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents)
{
  def index() = Action {
    Ok(appConfiguration.toJsonObject)
  }
}
