import scala.collection.mutable.{Map => MMap}

import unfiltered.request._
import unfiltered.response._
import unfiltered.directives._, Directives._

import LoginUtil._
import SearchUtil._
import ConnectUtil._
import QuestionUtil._

object Server {
  private implicit val cookie: Cookie = MMap()

  private val app = unfiltered.filter.Planify { Directive.Intent {
    case GET(Path(Seg("static" :: path))) =>
      success(Static(path))
    case GET(Path(Seg("api" :: "search" :: Nil))) =>
      for (
        key  <- data.as.String named "key";
        page <- data.as.Int named "page"
      ) yield (key, page) match {
        case (Some(key), Some(page)) => ResponseString(search(key, page))
        case _ => BadRequest
      }
    case GET(Path(Seg("api" :: "submit" :: Nil))) =>
      for (
        id   <- data.as.String named "id";
        qnum <- data.as.String named "qnum";
        ans  <- data.as.String named "ans";
        link <- data.as.String named "link"
      ) yield (id, qnum, ans, link) match {
        case (Some(id), Some(qnum), Some(ans), Some(link)) =>
          answer(id, qnum, ans, link); Ok
        case _ => BadRequest
      }
    case GET(Path(Seg("api" :: "question" :: Nil))) =>
      for (
        qnum <- data.as.Int named "qnum"
      ) yield qnum match {
        case Some(qnum) => ResponseString(questions(qnum))
        case _ => BadRequest
      }
    case GET(Path(Seg("api" :: _))) =>
      success(NotFound)
    case GET(Path(Seg(_))) =>
      success(Redirect("/static/index.html"))
  }}

  def main(args: Array[String]): Unit = args.toList match {
    case port :: id :: pw :: Nil =>
      Login(id, pw)
      unfiltered.jetty.Server.http(port.toInt).plan(app).run()
    case _ =>
  }
}
