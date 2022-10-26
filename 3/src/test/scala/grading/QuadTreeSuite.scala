package grading

import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.Tolerance._
import org.scalatest.tagobjects.Slow

class QuadTreeSuite extends AnyFunSuite with QuadTreeUtils with GradingSuite {

  import quadtree._

  import scala.math.sqrt

  val epsilon = 1e-5

  lazy val space2 = Space(0, 0, 1000, 10000)
  lazy val set2 = centeredPoints(space2, 500, 5000)
  lazy val q2 = QuadTree(64, space2, set2)

  lazy val space3 = Space(0, 0, 100000000, 100000000)
  lazy val set3 = centeredPoints(space3, 10000, 100000)
  lazy val q3 = QuadTree(128, space3, set3)

  test("large structure [3pts]") {
    checkStructure(
      q2,
      Node(
        Node(
          Leaf,
          Leaf,
          Node(
            Leaf,
            Leaf,
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf)),
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf))
          ),
          Node(
            Leaf,
            Leaf,
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf)),
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf))
          )
        ),
        Node(
          Leaf,
          Leaf,
          Node(
            Leaf,
            Leaf,
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf)),
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf))
          ),
          Node(
            Leaf,
            Leaf,
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf)),
            Node(Leaf, Leaf, Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf))
          )
        ),
        Node(
          Node(
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Leaf,
            Leaf
          ),
          Node(
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Leaf,
            Leaf
          ),
          Leaf,
          Leaf
        ),
        Node(
          Node(
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Leaf,
            Leaf
          ),
          Node(
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Node(Node(Leaf, Leaf, Leaf, Leaf), Node(Leaf, Leaf, Leaf, Leaf), Leaf, Leaf),
            Leaf,
            Leaf
          ),
          Leaf,
          Leaf
        )
      )
    )
  }

  test("large size [1pt]") {
    assert(q2.size === 5000)
  }

  test("huge size [1pt]") {
    assert(q3.size === 100000)
  }

  test("large get [1pt]") {
    assert(q2.get(Point(723, 5201)).contains(Point(723, 5201)))
    assert(q2.get(Point(905, 1312)).contains(Point(905, 1312)))
    assert(q2.get(Point(89, 122)).isEmpty)
  }

  test("huge get [1pt]") {
    assert(q3.get(Point(61674773, 48702170)).contains(Point(61674773, 48702170)))
    assert(q3.get(Point(406472, 258960)).contains(Point(406472, 258960)))
    assert(q3.get(Point(12345678, 12345678)).isEmpty)
  }

  test("large toMap [1pt]") {
    assert(q2.toMap === set2.map(x => x -> x).toMap)
  }

  test("huge toMap [1pt]") {
    assert(q3.toMap === set3.map(x => x -> x).toMap)
  }

  test("large clearSpace [2pts]") {
    val q = q2.clearSpace(Space(0, 5000, 1000, 10000))
    assert(q.size === 2480)
    assert(q.get(Point(723, 5201)).isEmpty)
    assert(q.get(Point(905, 1312)).contains(Point(905, 1312)))
  }

  test("huge clearSpace [2pts]") {
    val q = q3.clearSpace(Space(0, 0, 50000000, 100000000))
    assert(q.size === 50228)
    assert(q.get(Point(406472, 258960)).isEmpty)
    assert(q.get(Point(61674773, 48702170)).contains(Point(61674773, 48702170)))
  }

  test("large get space [2pts]") {
    assert(q2.get(Space(0, 0, 200, 2000)) === Map(Point(113, 1753) -> Point(113, 1753)))
  }

  test("huge get space [2pts]") {
    assert(q3.get(Space(0, 0, 500000, 500000)) ===
      Map(
        Point(172898, 396940) -> Point(172898, 396940),
        Point(406472, 258960) -> Point(406472, 258960),
        Point(499211, 230095) -> Point(499211, 230095)
      ))
  }

  test("large get within [2pts]") {
    assert(q2.getWithin(Point(0, 0), 1500) ===
      Map(Point(235, 1219) -> Point(235, 1219), Point(708, 626) -> Point(708, 626)))
  }

  test("huge get within [2pts]") {
    assert(q3.getWithin(Point(50000000, 50000000), 100000) ===
      Map(Point(49963831, 49932841) -> Point(49963831, 49932841)))
  }

  test("large get nearest filter [3pts]") {
    val (l, p, d) = q2.getNearest(Point(500, 5000), Some {
      case (_, p) => p.y % 100 == 0
    })
    assert(p === Point(440, 4900))
    assert(l === p)
    assert(d === sqrt(13600) +- epsilon)
  }

  test("huge get nearest filter [3pts]") {
    val (l, p, d) = q3.getNearest(Point(50000000, 50000000), Some {
      case (_, p) => p.y % 1000 == 0
    })
    assert(p === Point(44885683, 56949000))
    assert(l === p)
    assert(d === sqrt(74444839376489.0))
  }

  test("large get nearest [2pts]") {
    assert(q2.getNearest(Point(497, 5012))._3 === sqrt(2.0) +- epsilon)
    assert(q2.getNearest(Point(421, 1234))._3 === sqrt(34821.0) +- epsilon)
  }

  test("huge get nearest [2pts]") {
    assert(q3.getNearest(Point(61624321, 48654299))._3 === sqrt(4837036945.0) +- epsilon)
    assert(q3.getNearest(Point(12345678, 12345678))._3 === sqrt(37557717761.0) +- epsilon)
  }

  test("large remove [2pts]") {
    val q = set2.tail.foldLeft(q2)(_.remove(_))
    assert(q === QuadLeaf(64, space2, Map(set2.head -> set2.head)))
  }

  test("huge remove [2pts]", Slow) {
    val q = set3.tail.foldLeft(q3)(_.remove(_))
    assert(q === QuadLeaf(128, space3, Map(set3.head -> set3.head)))
  }

  test("large foreach [2pts]") {
    var n = 0
    q2 foreach {
      case (_, p) => n += 2 * p.x + p.y
    }
    assert(n === 29950650)
  }

  test("huge foreach [2pts]") {
    var n = 0L
    q3 foreach {
      case (_, p) => n += 2 * p.x + p.y
    }
    assert(n === 15032009034603L)
  }

  test("large filter [2pts]") {
    val q = q2 filter {
      case (_, p) => p.x == 494
    }
    assert(q.size === 9)
    checkStructure(q, Leaf)
  }

  test("huge filter [2pts]") {
    val q = q3 filter {
      case (_, p) => math.abs(p.x - p.y) < 10000
    }
    assert(q.size === 20)
    checkStructure(q, Leaf)
  }
}
