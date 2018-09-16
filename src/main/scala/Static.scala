import scala.io.Source

import unfiltered.response._

object Static {
  def apply(path: List[String]) =
    try {
      Ok ~> ResponseString(Source.fromFile(s"src/resources/${path.mkString("/")}", "UTF-8").mkString)
    } catch {
      case _: Exception => Pass
    }
}
