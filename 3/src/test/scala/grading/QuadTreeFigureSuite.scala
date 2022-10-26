package grading

import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.Tolerance._

// tests from the assignment's Fig. 1
class QuadTreeFigureSuite extends AnyFunSuite with QuadTreeUtils with GradingSuite {

  import quadtree._
  import Figure._

  val epsilon = 1e-5

  test("get (2) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.get(E).contains(E))
    assert(untouched(A, B, C, F))
  }

  test("get (3) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.get(Point(13, 1)).isEmpty)
    assert(untouched(A, B, C, D, E, F))
  }

  test("get (5) [3pts]") {
    resetAll(ABCDEF)
    assert(quad.get(Space(10, 0, 12, 2)) === Map(F -> F))
    assert(untouched(A, B, C, D, E))
  }

  test("get (6) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.get(Space(100, 100, 1000, 10000)).isEmpty)
    assert(untouched(A, B, C, D, E, F))
  }

  test("getNearest (4) [2pts]") {
    resetAll(ABCDEF)
    val (l, x, d) = quad.getNearest(Point(16, -1))
    assert(l === F)
    assert(x === F)
    assert(d === math.sqrt(29) +- epsilon)
    assert(untouched(A, B, C))
  }

  test("getWithin (2) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.getWithin(Point(11, -1), 6) === Map(E -> E, F -> F))
    assert(untouched(A, B))
  }

  test("add (3) [2pts]") {
    val X = Point(10, 7)
    assert(quad.add(X) === {
      val trtl = QuadLeaf(2, Space(8, 9, 11, 10), Map(A -> A))
      val trtr = QuadLeaf(2, Space(12, 9, 15, 10), Map.empty[Point, Point])
      val trbl = QuadLeaf(2, Space(8, 6, 11, 8), Map(X -> X))
      val trbr = QuadLeaf(2, Space(12, 6, 15, 8), Map(B -> B))
      val tr = QuadNode(2, Space(8, 6, 15, 10), trtl, trtr, trbl, trbr)
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("add (4) [2pts]") {
    val X = Point(9, 1)
    assert(quad.add(X) === {
      val brtl = QuadLeaf(2, Space(8, 3, 11, 5), Map(D -> D, E -> E))
      val brtr = QuadLeaf(2, Space(12, 3, 15, 5), Map.empty[Point, Point])
      val brbl = QuadLeaf(2, Space(8, 0, 11, 2), Map(F -> F, X -> X))
      val brbr = QuadLeaf(2, Space(12, 0, 15, 2), Map.empty[Point, Point])
      val br = QuadNode(2, Space(8, 0, 15, 5), brtl, brtr, brbl, brbr)
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("add (5) [2pts]") {
    val X = Point(10, 4)
    assert(quad.add(X) === {
      val brtltl = QuadLeaf(2, Space(8, 5, 9, 5), Map.empty[Point, Point])
      val brtltr = QuadLeaf(2, Space(10, 5, 11, 5), Map(D -> D))
      val brtlbl = QuadLeaf(2, Space(8, 3, 9, 4), Map(E -> E))
      val brtlbr = QuadLeaf(2, Space(10, 3, 11, 4), Map(X -> X))
      val brtl = QuadNode(2, Space(8, 3, 11, 5), brtltl, brtltr, brtlbl, brtlbr)
      val br = QuadNode(2, Space(8, 0, 15, 5), brtl, brtr, brbl, brbr)
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("remove (2) [1pt]") {
    assert(quad.remove(Point(10, 4)) === quad)
  }

  test("remove (4) [2pts]") {
    assert(quad.remove(E) === {
      val br = QuadLeaf(2, Space(8, 0, 15, 5), Map(D -> D, F -> F))
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("add (6) [3pts]") {
    val e: QuadTree[Point, Point] = QuadLeaf(2, space, Map.empty)
    val q = ABCDEF.foldLeft(e)(_.add(_))
    assert(q === quad)
  }

  test("remove (5) [3pts]") {
    val e: QuadTree[Point, Point] = QuadLeaf(2, space, Map.empty)
    val q = ABCDEF.foldLeft(quad)(_.remove(_))
    assert(q === e)
  }

  test("add (7) [3pts]") {
    val X = Point(10, 4)
    val e: QuadTree[Point, Point] = QuadLeaf(2, space, Map(X -> X))
    val q = ABCDEF.foldLeft(e)(_.add(_))
    assert(q.remove(X) === quad)
  }
}
