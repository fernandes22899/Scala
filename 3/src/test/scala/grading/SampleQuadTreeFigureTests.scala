package grading

import org.scalactic.Tolerance._
import org.scalatest.funsuite.AnyFunSuite

// tests from the assignment's Fig. 1
class SampleQuadTreeFigureTests extends AnyFunSuite with QuadTreeUtils {

  import Figure._
  import quadtree._

  val epsilon = 1e-5

  test("apply (1) [3pts]") {
    assert(QuadTree(2, space, ABCDEF) == quad)
  }

  test("size [2pts]") {
    assert(quad.size === 6)
  }

  test("space [1pt]") {
    assert(quad.space.minX === 0)
    assert(quad.space.minY === 0)
    assert(quad.space.maxX === 15)
    assert(quad.space.maxY === 10)
  }

  test("isEmpty [1pt]") {
    assert(tl.isEmpty)
    assert(brtr.isEmpty)
    assert(!bl.isEmpty)
    assert(!brbl.isEmpty)
  }

  test("get (1) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.get(A).contains(A))
    assert(untouched(C, D, E, F))
  }

  test("get (4) [3pts]") {
    resetAll(ABCDEF)
    assert(quad.get(Space(8, 6, 14, 14)) === Map(A -> A, B -> B))
    assert(untouched(C, D, E, F))
  }

  test("getNearest (1) [2pts]") {
    resetAll(ABCDEF)
    val (l, p, d) = quad.getNearest(Point(5, 4))
    assert(l === C)
    assert(p === C)
    assert(d === 1.0 +- epsilon)
    assert(untouched(A, B, D, E, F))
  }

  test("getNearest (2) [2pts]") {
    resetAll(ABCDEF)
    val (l, p, d) = quad.getNearest(Point(10, 7))
    assert(l === D)
    assert(p === D)
    assert(d === 2.0 +- epsilon)
    assert(untouched(F))
  }

  test("getNearest (3) [2pts]") {
    resetAll(ABCDEF)
    val (l, p, d) = quad.getNearest(Point(9, 5))
    assert(l === D)
    assert(p === D)
    assert(d === 1.0 +- epsilon)
    assert(untouched(C, F))
  }

  test("getWithin (1) [2pts]") {
    resetAll(ABCDEF)
    assert(quad.getWithin(Point(7, 9), 2.1) === Map(A -> A))
    assert(untouched(C, D, E, F))
  }

  test("toMap [2pts]") {
    assert(quad.toMap === allPoints)
  }

  test("foreach [2pts]") {
    val s = List.newBuilder[Point]
    quad.foreach { case (_, p) => s += p }
    assert(s.result() === ABCDEF)
  }

  test("filter [3pts]") {
    val q = quad filter {
      case (_, p) => p.x == p.y
    }
    assert(q === QuadLeaf(2, space, Map(A -> A)))
  }

  test("QuadTree map [3pts]") {
    val q = quad map (_.toString)
    assert(q === {
      val tl = QuadLeaf(2, Space(0, 6, 7, 10), Map.empty[Point, String])
      val tr = QuadLeaf(2, Space(8, 6, 15, 10), Map(A -> "Point(9,9)", B -> "Point(13,8)"))
      val bl = QuadLeaf(2, Space(0, 0, 7, 5), Map(C -> "Point(6,4)"))
      val brtl = QuadLeaf(2, Space(8, 3, 11, 5), Map(D -> "Point(10,5)", E -> "Point(9,3)"))
      val brtr = QuadLeaf(2, Space(12, 3, 15, 5), Map.empty[Point, String])
      val brbl = QuadLeaf(2, Space(8, 0, 11, 2), Map(F -> "Point(11,1)"))
      val brbr = QuadLeaf(2, Space(12, 0, 15, 2), Map.empty[Point, String])
      val br = QuadNode(2, Space(8, 0, 15, 5), brtl, brtr, brbl, brbr)
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("add (1) [1pt]") {
    intercept[IllegalArgumentException] {
      quad.add(Point(3, 30))
    }
  }

  test("add (2) [2pts]") {
    val X = Point(5, 7)
    assert(quad.add(X) === {
      val tl = QuadLeaf(2, Space(0, 6, 7, 10), Map(X -> X))
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }

  test("remove (1) [1pt]") {
    assert(quad.remove(Point(3, 30)) === quad)
  }

  test("remove (3) [2pts]") {
    assert(quad.remove(B) === {
      val tr = QuadLeaf(2, Space(8, 6, 15, 10), Map(A -> A))
      QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
    })
  }
}
