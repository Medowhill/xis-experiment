import scala.io.Source

import java.io.FileWriter

object QuestionUtil {

  private val file = "result"
  private val end = """{"fin": true}"""

  val questions =
    Source.fromFile("src/resources/questions", "UTF-8").mkString
      .split("\n").toList
      .map(l => l.split("\t").toList match {
        case q :: t :: Nil =>
          """{"fin": false,"question": """" + q + """","type": """" + t + "\"}"
        case _ => end
      }) :+ end

  def answer(id: String, qnum: String, ans: String, link: String) = {
    val fw = new FileWriter(file, true)
    try {
      fw.write(s"${id}\t${qnum}\t${ans}\t${link}\t${System.currentTimeMillis}\n")
    } finally fw.close
  }
}
