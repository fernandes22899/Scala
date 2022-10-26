package grading

import org.scalactic.Tolerance._
import org.scalatest.funsuite.AnyFunSuite

class SampleQuadTreeTests extends AnyFunSuite with QuadTreeUtils {

  import quadtree._

  import scala.math.sqrt

  val epsilon = 1e-5

  lazy val space1 = Space(0, 100, 100, 200)
  lazy val set1 = uniformPoints(space1, 1000)
  lazy val q1 = QuadTree(32, space1, set1)

  test("small structure [3pts]") {
    checkStructure(
      q1,
      Node(
        Node(
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf)
        ),
        Node(
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf)
        ),
        Node(
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf)
        ),
        Node(
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf),
          Node(Leaf, Leaf, Leaf, Leaf)
        )
      )
    )
  }

  test("small size [1pt]") {
    assert(q1.size === 1000)
  }

  test("small get [1pt]") {
    assert(q1.get(Point(89, 121)).contains(Point(89, 121)))
    assert(q1.get(Point(23, 162)).contains(Point(23, 162)))
    assert(q1.get(Point(89, 122)).isEmpty)
  }

  test("small toMap [1pt]") {
    assert(q1.toMap === set1.map(x => x -> x).toMap)
  }

  test("small clearSpace [2pts]") {
    val q = q1.clearSpace(Space(0, 100, 50, 200))
    assert(q.size === 520)
    assert(q.get(Point(89, 121)).contains(Point(89, 121)))
    assert(q.get(Point(23, 162)).isEmpty)
  }

  test("small get space [2pts]") {
    assert(q1.get(Space(0, 100, 5, 105)) ===
      Map(
        Point(3, 101) -> Point(3, 101),
        Point(3, 104) -> Point(3, 104),
        Point(3, 100) -> Point(3, 100)
      ))
  }

  test("small get within [2pts]") {
    assert(q1.getWithin(Point(0, 100), 5) ===
      Map(
        Point(3, 101) -> Point(3, 101),
        Point(3, 104) -> Point(3, 104),
        Point(3, 100) -> Point(3, 100)
      ))
  }

  test("small get nearest [2pts]") {
    assert(q1.getNearest(Point(40, 123))._3 === sqrt(8.0) +- epsilon)
    assert(q1.getNearest(Point(101, 201))._3 === sqrt(10.0) +- epsilon)
  }

  test("small get nearest filter [3pts]") {
    assert(q1.getNearest(Point(40, 123), Some {
      case (_, p) => p.y % 10 == 0
    })._2 === Point(42, 120))
  }

  test("small remove [2pts]") {
    val q = set1.tail.foldLeft(q1)(_.remove(_))
    assert(q === QuadLeaf(32, space1, Map(set1.head -> set1.head)))
  }

  test("small foreach [2pts]") {
    var n = 0
    q1 foreach {
      case (_, p) => n += 2 * p.x + p.y
    }
    assert(n === 252542)
  }

  test("small filter [2pts]") {
    val q = q1 filter {
      case (_, p) => p.x == 42
    }
    assert(q.size === 7)
    checkStructure(q, Leaf)
  }
}
