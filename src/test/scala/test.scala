import EgyezoketKeres._

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class EgyezoketKeresSpec extends FunSpec with ShouldMatchers {
  describe("read") {
    it("head,a1,,a2,") {
      read(List("head,a1,,a2,").iterator) should equal(Map("head" -> Vector(Some("a1"), None, Some("a2"), None)))
    }
    it("head,a1,,a2") {
      read(List("head,a1,,a2").iterator) should equal(Map("head" -> Vector(Some("a1"), None, Some("a2"))))
    }
    it("head,a1,,a2,,") {
      read(List("head,a1,,a2,,").iterator) should equal(Map("head" -> Vector(Some("a1"), None, Some("a2"), None, None)))
    }
  }
  describe("mat") {
    it("2x2") {
      val r = read(List("head1,a1,,a2,", "head2,a1,a0,a3,a4").iterator)
      mat(r) should equal(
        Map(("head1", "head2") -> (1, Vector("a1")),
          ("head2", "head1") -> (1, Vector("a1"))
        )
      )
    }
  }

  describe("pelda") {
    val s = """1,1,,3,4,5,,,8,,10
|2,,2,,4,,6,7,8,9,
|3,,2,3,,5,,7,,9,10
|4,1,,3,4,5,,,8,,10
|5,1,2,,4,,6,7,,9,
|6,12,2,231,4,412,6,7,232,9,
""".stripMargin
    val lines = read(io.Source.fromString(s).getLines)
    val matrix = mat(lines)

    it("6 ismervben az 1. es a 4.") {
      select(matrix, 6) should equal(Map(Vector("1", "3", "4", "5", "8", "10") -> Vector("1", "4")))
    }
    it("5 ismervben az (2, 5,6) es (1,4)") {
      select(matrix, 5) should equal(Map(Vector("2", "4", "6", "7", "9") -> Vector("2", "5", "6")))
    }
    it("string output") {
      val str = selectedToString(select(matrix, 5))
      str should equal("""(2,4,6,7,9) (2,5,6)""".stripMargin)

    }
  }

  describe("pelda 2 ") {
    val s = """1,,,,,5,6,,,9,,,12,
|2,1,,,,5,6,,,,10,,12,13
|3,1,,,,,6,,8,,,,12,13
|4,,,,,5,6,,,,10,,12,13
|5,1,,,,5,6,,,,,,12,
|6,1,2,,,5,6,,,,,,12,
""".stripMargin
    val lines = read(io.Source.fromString(s).getLines)
    val matrix = mat(lines)

    it("4 ismervben ") {

      select(matrix, 4) should equal(Map(Vector("1", "6", "12", "13") -> Vector("2", "3"),
        Vector("1", "5", "6", "12") -> Vector("2", "5", "6")))
    }

    it("5 ismervben ") {

      select(matrix, 5) should equal(Map(Vector("5", "6", "10", "12", "13") -> Vector("2", "4")))
    }

    it("6 ismervben ") {
      select(matrix, 6) should equal(Map())
    }

  }
}