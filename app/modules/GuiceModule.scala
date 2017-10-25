package modules

import com.google.inject.AbstractModule
import dao.{AccountDAO, RelationalAccountDAO}

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    bind(classOf[AccountDAO]).to(classOf[RelationalAccountDAO])
  }
}
