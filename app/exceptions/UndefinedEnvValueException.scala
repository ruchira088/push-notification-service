package exceptions

case class UndefinedEnvValueException(name: String) extends Exception
{
  override def getMessage = s"$name is NOT defined in the environment."
}