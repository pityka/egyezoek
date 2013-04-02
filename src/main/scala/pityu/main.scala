import java.io._
import scala.language.reflectiveCalls
import scala.language.postfixOps

object EgyezoketKeres extends App {

  def useResource[A <: { def close(): Unit }, B](param: A)(f: A => B): B =
    try { f(param) } finally {
      param.close()
    }

  def writeToFile(fileName: String, data: java.lang.String) = useResource(new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) { writer =>
    writer.write(data)
  }

  def pairwise(x: Vector[Option[String]], y: Vector[Option[String]]): (Int, Vector[String]) = {
    val eq = x zip y filter { case (a, b) => a.isDefined && b.isDefined && a.get == b.get } map { case (a, b) => (a.get) }
    (eq.size, eq.toVector)
  }

  def mat(d: Map[String, Vector[Option[String]]]): Map[(String, String), (Int, Vector[String])] =
    (for {
      i <- d.keys
      j <- d.keys if j != i
    } yield (i, j) -> pairwise(d(i), d(j))).toMap

  def select(m: Map[(String, String), (Int, Vector[String])], t: Int): Map[Vector[String], Vector[String]] = {
    m.filter(_._2._1 == t).toSeq.groupBy(_._2._2).map(x => x._1 -> x._2.map(x => Vector(x._1._1, x._1._2)).toVector.flatten.distinct.sorted)
  }

  def selectedToString(selectedLines: Map[Vector[String], Vector[String]]) = selectedLines.map {
    case (feature, egyezesek) =>
      feature.mkString("(", ",", ")") + " " + egyezesek.mkString("(", ",", ")")
  }.mkString("\n")

  def read(lines: Iterator[String]): Map[String, Vector[Option[String]]] =
    lines.map { l =>
      val spl = l.split(",", -2)
      spl.head -> spl.tail.toVector.map(s => if (s.size == 0) None else Some(s))
    }.toMap

  if (args.size != 2) {
    println("Hasznalat: elso argumentum a fajl neve. a fajl vesszovel , van elvalasztva. elso oszlop fejlec, a tobbi adat. masodik argument a kimeneti fajl tove. Ha a masodik argumentum egy kotojel (-), akkor azt stdoutra ir")
    System.exit(0)
  } else {
    val file = args.head
    val out = args(1)
    val lines = read(io.Source.fromFile(file).getLines)
    val matrix = mat(lines)

    val matrixStr = "Tavolsagmatrix:\nhead1 head2 #egyezesek egyezo_ismervek\n" + matrix.map(x => s"${x._1._1} ${x._1._2} ${x._2._1} ${x._2._2.mkString("(", ",", ")")}").mkString("\n")

    val selectedLines = for (i <- 4 to 10) yield {
      i -> select(matrix, i)
    }

    val str = "Szintenkenti egyezesek:\n(egyezo_ismervek) (egyezo sorok)\n" + selectedLines.map(x => s"Szint: ${x._1} \n" + selectedToString(x._2)).mkString("\n")

    if (out != "-") {
      writeToFile(out + ".matrix.txt", matrixStr)

      writeToFile(out + ".egyezoek.txt", str)
    } else {
      println(matrixStr)
      println(str)
    }

  }

}

