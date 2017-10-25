package utils

import java.io.{File, FileNotFoundException}

import scala.util.{Failure, Success, Try}

object FileUtils
{
  def getFile(filePath: String): Try[File] =
  {
    val file = new File(filePath)

    if (file.exists())
      Success(file)
    else
      Failure(new FileNotFoundException(filePath))
  }
}
