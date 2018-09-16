import java.net.URLDecoder

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import ConnectUtil._

object SearchUtil {
  private val url = "https://search.kaist.ac.kr/index.jsp"

  def search(keyword: String, index: Int)(implicit cookie: Cookie): String = {
    val key = URLDecoder.decode(keyword, "UTF-8")
    val doc = get(url, Map("searchTerm" -> key, "searchTarget" -> "portal", "currentPage" -> index.toString))._1
    doc >> elementList(".section_body") match {
      case body :: _ =>
        val subjects = (body >> elementList(".subject")).flatMap(_ >> elementList("a"))
        val previews = body >> elementList(".cont_txt") >> allText("span")
        (subjects zip previews).map {
          case (s, p) =>
            val t = s >> allText("a")
            val l = s >> attr("href")("a")
            """<a href="""" + l + """" target="_blank">""" + t + """</a><p>""" + p + """</p>"""
        }.mkString("")
      case Nil => ""
    }
  }
}
