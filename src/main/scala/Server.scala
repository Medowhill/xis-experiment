import scala.collection.mutable.{Map => MMap}

import unfiltered.request._
import unfiltered.response._
import unfiltered.directives._, Directives._

import LoginUtil._
import SearchUtil._
import ConnectUtil._

object Server {
  private implicit val cookie: Cookie = MMap()

  private val app = unfiltered.filter.Planify { Directive.Intent {
    case GET(Path(Seg("static" :: path))) =>
      success(Static(path))
    case GET(Path(Seg("api" :: path))) => success(path match {
      case "search" :: keyword :: page :: Nil => 
        Ok ~> ResponseString(search(keyword, page.toInt))
      case _ =>
        NotFound
    })
    case GET(Path(Seg(_))) =>
      success(Redirect("/static/index.html"))
  }}

  def main(args: Array[String]): Unit = args.toList match {
    case id :: pw :: Nil =>
      Login(id, pw)
      unfiltered.jetty.Server.http(80).plan(app).run()
    case _ =>
  }
}
