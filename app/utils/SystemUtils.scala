package utils

import org.apache.commons.lang3.StringUtils

object SystemUtils
{
  def printErrorMessage(message: String): Unit = System.err.println(message)

  def terminate(throwable: Throwable, serviceMessage: String = StringUtils.EMPTY): Unit =
  {
    printErrorMessage(throwable.getMessage)
    printErrorMessage(serviceMessage)
    System.exit(1)
  }
}
