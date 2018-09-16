import scala.collection.mutable.Buffer

import ConnectUtil._

object LoginUtil {
  private val loginUrl = "https://portalsso.kaist.ac.kr/ssoProcess.ps"
  private val keyCookie = "Set-Cookie"
  private val keyLocation = "Location"

  def Login(id: String, pw: String)(implicit cookie: Cookie): Unit = {
      val res = post(loginUrl, Map("userId" -> id, "password" -> pw))
      cookie ++= readCookie(res)
      cookie ++= readCookie(get(readRedirection(res)))
  }

  private def readCookie(res: Result): Buffer[(String, String)] = res._2(keyCookie).map(s => {
    val i = s.indexOf(";")
    val s1 = if (i == -1) s else s.substring(0, i)
    val i1 = s1.indexOf("=")
    if (i1 == -1) (s1, "") else (s1.substring(0, i1), s1.substring(i1 + 1))
  })

  private def readRedirection(res: Result): String = res._2(keyLocation).head
}
