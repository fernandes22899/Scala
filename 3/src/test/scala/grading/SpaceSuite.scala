package grading

import org.scalatest.funsuite.AnyFunSuite

import scala.annotation.tailrec

class SpaceSuite extends AnyFunSuite with GradingSuite {

  import quadtree.Space
  import Figure._

  test("center (1) [1pt]") {
    assert(space.center === (7, 5))
  }

  test("split (1) [1pt]") {
    val (tl, tr, bl, br) = space.split
    assert(tl === Space(0, 6, 7, 10))
    assert(tr === Space(8, 6, 15, 10))
    assert(bl === Space(0, 0, 7, 5))
    assert(br === Space(8, 0, 15, 5))
  }

  test("split (4) [1pt]") {
    val (tl, tr, bl, br) = Space(0, 100, 100, 200).split
    assert(tl === Space(0, 151, 50, 200))
    assert(tr === Space(51, 151, 100, 200))
    assert(bl === Space(0, 100, 50, 150))
    assert(br === Space(51, 100, 100, 150))
  }

  test("BL contains center [1pt]") {
    val space = Space(Int.MaxValue, Int.MaxValue)
    val zero = Space(0, 0)
    @tailrec
    def check(sp: Space): Boolean = sp == zero || {
      val (_, _, bl, _) = sp.split
      val (x, y) = sp.center
      sp != bl && bl.contains(x, y) && check(bl)
    }
    assert(check(space))
  }

  test("contains (1) [1pt]") {
    for (x <- space.minX to space.maxX; y <- space.minY to space.maxY) {
      assert(space.contains(x, y))
      assert(space.contains(Point(x, y)))
    }
  }

  test("contains (2) [1pt]") {
    assert(!space.contains(2, 11))
    assert(!space.contains(2, -1))
    assert(!space.contains(-1, 7))
    assert(!space.contains(16, 7))
  }

  test("intersects (1) [1pt]") {
    assert(space.intersects(Space(-5, -5, 7, 2)))
    assert(space.intersects(Space(-5, 5, 7, 7)))
    assert(space.intersects(Space(9, 7, 20, 12)))
    assert(space.intersects(Space(10, -7, 17, 3)))
  }

  test("intersects (3) [1pt]") {
    assert(space.intersects(Space(-1, 5, 20, 7)))
    assert(space.intersects(Space(5, -1, 7, 12)))
    assert(space.intersects(Space(-1, 5, 20, 5)))
    assert(space.intersects(Space(5, -1, 5, 12)))
    assert(space.intersects(Space(-5, -4, 20, 12)))
    assert(space.intersects(Space(3, 4, 5, 6)))
  }

  test("bounds (2) [1pt]") {
    assert(Space.bounds(List.empty).isEmpty)
  }

  test("various [5pts]") {
    val (tl, tr, bl, br) = space.split
    assert(!tl.intersects(tr))
    assert(!tl.intersects(bl))
    assert(!tl.intersects(br))
    assert(!tr.intersects(bl))
    assert(!tr.intersects(br))
    assert(!bl.intersects(br))
    for (p <- ABCDEF)
      assert(space.contains(p))
    assert(tr.contains(A))
    assert(tr.contains(B))
    assert(bl.contains(C))
    assert(br.contains(D))
    assert(br.contains(E))
    assert(br.contains(F))
    assert(bl.contains(7, 5))
    assert(bl.contains(0, 0))
    assert(tl.contains(0, 10))
    assert(tr.contains(15, 10))
    assert(br.contains(15, 0))
    val (brtl, _, brbl, _) = br.split
    assert(brtl.contains(D))
    assert(brtl.contains(E))
    assert(brbl.contains(F))
  }
}
