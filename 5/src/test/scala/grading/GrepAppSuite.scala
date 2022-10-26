package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow
import org.scalatest.time.SpanSugar._

import scala.io.Source
import scala.util.Using

class GrepAppSuite extends AnyFunSuite with GradingRun {

  override val longTimeLimit = 2.seconds

  import parallel.GrepApp
  import parallel.GrepApp.TextSource

  val alice = Using.resource(Source.fromResource("alice.txt"))(_.getLines().toList)
  val usconst = Using.resource(Source.fromResource("usconst.txt"))(_.getLines().toList)

  def aliceText: TextSource = new TextSource {
    val name = "alice.txt"
    val lines = alice.iterator
  }

  def usconstText: TextSource = new TextSource {
    val name = "usconst.txt"
    val lines = usconst.iterator
  }

  test("1 file, 1 match") {
    val app = new GrepApp(0, 1.second)
    app.find("chop.*head".r, List(aliceText)) match {
      case List(res) =>
        assert(res.name === "alice.txt")
        assert(res.line === "'Talking of axes,' said the Duchess, 'chop off her head!'")
        assert(res.lineNumber === 1354)
      case _ => fail("incorrect length")
    }
  }

  test("1 file, no match") {
    val app = new GrepApp(0, 1.second)
    assert(app.find("(?i:education)".r, List(usconstText)).isEmpty)
  }

  test("1 file, multiple matches") {
    val app = new GrepApp(0, 1.second)
    app.find("Off.*her head".r, List(aliceText)) match {
      case l if l.lengthIs == 3 =>
        assert(l.forall(_.name == "alice.txt"))
        assert(l.map(_.lineNumber).toSet === Set(1964, 2059, 3272))
      case _ => fail("incorrect length")
    }
  }

  test("2 files") {
    val app = new GrepApp(0, 1.second)
    val l = app.find("(?i:vote)".r, List(aliceText, usconstText))
    assert(l.length === 35)
    val al = l.filter(_.name == "alice.txt")
    assert(al.length === 1)
    assert(al.head.lineNumber === 1734)
    assert(l.map(_.lineNumber).toSet ===
      Set(56, 73, 147, 155, 286, 288, 289, 293, 296, 299, 300, 304, 305, 309, 600, 603, 604, 605,
        606, 611, 613, 616, 618, 619, 627, 655, 672, 687, 702, 729, 813, 828, 856, 863, 1734))
  }

  test("100 files", Slow) {
    val app = new GrepApp(0, 2.seconds)
    val files = List.range(0, 50).flatMap(_ => List(aliceText, usconstText))
    val l = app.find("shire".r, files)
    assert(l.count(_.name == "alice.txt") === 7 * 50)
    assert(l.count(_.name == "usconst.txt") === 2 * 50)
    assert(l.length === (7 + 2) * 50)
    assert(l.count(_.lineNumber == 42) === 50)
    assert(l.count(_.lineNumber == 2104) === 50)
    assert(l.exists(_.line.contains("Cheshire")))
    assert(l.exists(_.line.contains("Hampshire")))
  }

  test("100_000 files", Slow) {
    val app = new GrepApp(16, 2.seconds)
    val files = List.range(0, 500).flatMap(_ => List(aliceText, usconstText))
    val l = app.find("shire".r, files)
    assert(l.count(_.name == "alice.txt") === 7 * 500)
    assert(l.count(_.name == "usconst.txt") === 2 * 500)
    assert(l.length === (7 + 2) * 500)
  }
}
