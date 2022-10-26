package grading

import org.scalatest.funsuite.AnyFunSuite

class SampleSpaceTests extends AnyFunSuite {

  import Figure._
  import quadtree.Space

  // a one dimensional space
  lazy val line = Space(1, 0, 14, 0)

  // a tiny space
  lazy val point = Space(7, 0, 7, 0)

  test("isEmpty [1pt]") {
    assert(!space.isEmpty)
    assert(!line.isEmpty)
    assert(!point.isEmpty)
    assert(!Space(42, 42, 42, 42).isEmpty)
    assert(Space(5, 4, 1, 6).isEmpty)
    assert(Space(4, 5, 7, 3).isEmpty)
  }

  test("center (2) [1pt]") {
    assert(point.center === (7, 0))
    assert(line.center === (7, 0))
    assert(point.center === (7, 0))
  }

  test("split (2) [1pt]") {
    val (tl, tr, bl, br) = line.split
    assert(tl === Space(1, 1, 7, 0))
    assert(tr === Space(8, 1, 14, 0))
    assert(bl === Space(1, 0, 7, 0))
    assert(br === Space(8, 0, 14, 0))
  }

  test("split (3) [1pt]") {
    val (tl, tr, bl, br) = point.split
    assert(tl === Space(7, 1, 7, 0))
    assert(tr === Space(8, 1, 7, 0))
    assert(bl === Space(7, 0, 7, 0))
    assert(br === Space(8, 0, 7, 0))
  }

  test("contains (3) [1pt]") {
    assert(line.contains(1, 0))
    assert(line.contains(7, 0))
    assert(point.contains(7, 0))
  }

  test("intersects (2) [1pt]") {
    assert(space.intersects(space))
    assert(space.intersects(line))
    assert(line.intersects(space))
    assert(space.intersects(point))
    assert(point.intersects(space))
    assert(point.intersects(line))
    assert(line.intersects(point))
    assert(point.intersects(point))
  }

  test("bounds (1) [3pts]") {
    assert(Space.bounds(ABCDEF) === Space(6, 1, 13, 9))
    assert(Space.bounds(List(A, E)) === Space(9, 3, 9, 9))
    assert(Space.bounds(List(A)) === Space(9, 9, 9, 9))
  }
}
