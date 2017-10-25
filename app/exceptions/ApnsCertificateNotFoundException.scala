package exceptions

import java.io.FileNotFoundException

case class ApnsCertificateNotFoundException(certificatePath: String) extends FileNotFoundException