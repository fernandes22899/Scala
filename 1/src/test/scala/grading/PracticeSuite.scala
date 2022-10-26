package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.tagobjects.Slow
import org.scalatest.time.SpanSugar._

class PracticeSuite extends SamplePracticeTests with GradingRun {

  override val longTimeLimit = 10.seconds

  import cs671.practice._

  import scala.util.Random

  test("find42 (2)", Slow) {
    assert(find42(new Random(8949057415L)) === 666_304_536)
  }

  test("findBig (2)") {
    assert(findBig(new Random(671L)) === 2_138_303_625)
  }

  test("findBig (3)") {
    assert(findBig(new Random(689433122L)) === 1_000_001)
  }

  test("findBig (4)") {
    assert(findBig(new Random(3271271015L)) === 550_128_456)
  }

  test("removeLongerThan (2)") {
    val l = List("How", "I", "want", "a", "drink")
    val r = removeLongerThan(l, 3)
    assert(r === List("How", "I", "a"))
  }

  test("removeLongerThan (3)") {
    val l = List("How", "I", "want", "a", "drink")
    val r = removeLongerThan(l, 4)
    assert(r === List("How", "I", "want", "a"))
  }

  test("removeLongerThan (4)") {
    val l = List("How", "I", "want", "a", "drink")
    val r = removeLongerThan(l, 5)
    assert(r === l)
  }

  test("removeLongerThan (5)") {
    val l = List("How", "I", "want", "a", "drink")
    val r = removeLongerThan(l, 0)
    assert(r.isEmpty)
  }

  test("removeLongerThan (6)") {
    assert(removeLongerThan(Nil, 42).isEmpty)
  }

  test("makePairs (2)") {
    assert(makePairs(Nil).isEmpty)
  }

  test("makePairs (3)") {
    assert(makePairs(List("")) === List(("", 0)))
  }

  test("makePairs (4)") {
    val l = List.iterate("", 1000)(str => str + ".")
    for (((str, len), i) <- makePairs(l).zipWithIndex) {
      assert(len === i)
      assert(str.length === i)
      assert(str.toSet.subsetOf(Set('.')))
    }
  }

  test("nextLetter (2)") {
    val rand = new Random(1)
    assert(nextLetter(rand) === 'R')
    assert(nextLetter(rand) === 'A')
    assert(nextLetter(rand) === 'H')
  }

  test("makeLetters (2)") {
    val l = makeLetters(new Random(1), 100)
    assert(
      l.mkString === "RAHJMYUWWKRXNFMQGEEBEOAPEZSDZSPMQCXJTGDYXKRPVMWMMPMPYLWRKVMEOZGBOQAYHUFOJCMXGHPTEQRGFNZDJSJGGWXHTNSK"
    )
  }

  test("makeLetters (3)") {
    assertThrows[IllegalArgumentException](makeLetters(new Random(1), -100))
  }

  test("getIfValid (3)") {
    val ref = new CountingRefImpl(42)
    assert(getIfValid(ref) contains 42)
    assert(ref.accessCount === 1)
  }

  test("makeRefs (2)") {
    val a = List.range(0, 1000)
    val b = makeRefs(a).map(_.get())
    assert(a === b)
  }

  test("getRefs (2)") {
    val l = List.tabulate(1000)(new CountingRefImpl(_))
    assert(getRefs(l) === List.range(0, 1000))
  }

  test("getValidRefs (2)") {
    val r1 = new CountingRefImpl("foo")
    val r2 = new CountingRefImpl("bar")
    r1.disable()
    r2.disable()
    assert(getValidRefs(List(r1, r2)).isEmpty)
  }

  test("getValidRefs (3)") {
    val rand = new Random(671)
    val refs = for (r <- makeRefs(List.range(0, 1000))) yield {
      if (rand.nextFloat() < 0.95f) r.disable()
      r
    }
    assert(
      getValidRefs(refs) === List(
        9, 22, 62, 98, 122, 143, 145, 159, 171, 180, 181, 212, 230, 231, 246, 250, 273, 306, 323,
        335, 359, 369, 383, 385, 396, 411, 415, 471, 476, 507, 518, 575, 595, 599, 623, 634, 662,
        667, 670, 683, 696, 749, 750, 781, 782, 829, 889, 893, 897, 930, 942, 967, 972
      )
    )
  }
}
