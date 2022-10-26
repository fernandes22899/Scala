package grading

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow
import scala.util.Random

class SamplePracticeTests extends AnyFunSuite {

  import cs671.practice._

  test("find42 (1)", Slow) {
    assert(find42(new Random(671)) === 182406286)
  }

  test("findBig (1)") {
    assert(findBig(new Random(671)) === 2138303625)
  }

  test("removeLongerThan (1)") {
    val l = List("How", "I", "want", "a", "drink")
    val r = removeLongerThan(l, 1)
    assert(r === List("I", "a"))
  }

  test("makePairs (1)") {
    val l = List("foo", "bar", "foobar")
    assert(makePairs(l) === List(("foo", 3), ("bar", 3), ("foobar", 6)))
  }

  test("nextLetter (1)") {
    val rand = new Random(671)
    assert(nextLetter(rand) === 'D')
    assert(nextLetter(rand) === 'S')
    assert(nextLetter(rand) === 'O')
  }

  test("makeLetters (1)") {
    val l = makeLetters(new Random(671), 10)
    assert(l.mkString === "DSOKFNVEMT")
  }

  test("getIfValid (1)") {
    val ref: CountingRef[Int] = new CountingRefImpl(42)
    assert(getIfValid(ref) contains 42)
    ref.disable()
    assert(getIfValid(ref).isEmpty)
  }

  test("getIfValid (2)") {
    val ref: CountingRef[Int] = new CountingRef[Int] {
      def accessCount = 0
      def reset()     = ()
      def disable()   = ()
      def get()       = 42
    }
    assert(getIfValid(ref) contains 42)
  }

  test("makeRefs (1)") {
    val l = makeRefs(List("A", "B", "C"))
    assert(l(0).get() === "A")
    assert(l(1).get() === "B")
    assert(l(2).get() === "C")
  }

  test("getRefs (1)") {
    val l: List[CountingRef[String]] = List(
      new CountingRefImpl("A"),
      new CountingRefImpl("B"),
      new CountingRefImpl("C")
    )
    assert(getRefs(l) === List("A", "B", "C"))
  }

  test("getValidRefs (1)") {
    val r1: CountingRef[String] = new CountingRefImpl("foo")
    val r2: CountingRef[String] = new CountingRefImpl("bar")
    r2.disable()
    assert(getValidRefs(List(r1, r2)) === List("foo"))
  }
}
