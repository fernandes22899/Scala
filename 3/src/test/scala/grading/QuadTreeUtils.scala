package grading

import org.scalatest.Suite

trait QuadTreeUtils { self: Suite =>

  import quadtree.{ Location, QuadLeaf, QuadNode, QuadTree, Space }

  import scala.util.Random

  def untouched(points: Point*): Boolean = points.forall(_.isUntouched)
  def resetAll(points: Seq[Point]) = points.foreach(_.reset())

  trait Tree
  case class Node(tl: Tree, tr: Tree, bl: Tree, br: Tree) extends Tree
  case object Leaf extends Tree

  def q2Tree[Loc <: Location[Loc], A](q: QuadTree[Loc, A]): Tree = q match {
    case QuadLeaf(_, _, _)              => Leaf
    case QuadNode(_, _, tl, tr, bl, br) => Node(q2Tree(tl), q2Tree(tr), q2Tree(bl), q2Tree(br))
  }

  def checkStructure[Loc <: Location[Loc], A](q: QuadTree[Loc, A], t: Tree): Unit = q match {
    case QuadNode(_, _, tl, tr, bl, br) => t match {
      case Node(a, b, c, d) =>
        checkStructure(tl, a)
        checkStructure(tr, b)
        checkStructure(bl, c)
        checkStructure(br, d)
      case _ => fail(s"$q should be a leaf")
    }
    case QuadLeaf(_, _, _) => t match {
      case Leaf => ()
      case _    => fail(s"$q should be an internal node")
    }
  }

  // could loop forever, use with care
  def uniformPoints(space: Space, n: Int): Set[Point] = {
    val minX = space.minX
    val minY = space.minY
    val rangeX = space.maxX - minX + 1
    val rangeY = space.maxY - minY + 1
    val rand = new Random(2015)
    val s = scala.collection.mutable.Set.empty[Point]
    var size = 0
    do {
      val x = rand.nextInt(rangeX) + minX
      val y = rand.nextInt(rangeY) + minY
      if (s.add(Point(x, y)))
        size += 1
    } while (size < n)
    s.toSet
  }

  // could loop forever, use with care
  def centeredPoints(space: Space, spread: Double, n: Int): Set[Point] = {
    val minX = space.minX
    val minY = space.minY
    val rangeX = space.maxX - minX + 1
    val rangeY = space.maxY - minY + 1
    val centerX = (minX + space.maxX) / 2
    val centerY = (minY + space.maxY) / 2
    val rand = new Random(2015)
    val s = scala.collection.mutable.Set.empty[Point]
    var size = 0
    do {
      val x = rand.nextInt(rangeX) + minX
      val y = rand.nextInt(rangeY) + minY
      val p = Point(x, y)
      if (!s(p)) {
        val a = x - centerX
        val b = y - centerY
        val e = math.exp(-math.sqrt(a * a + b * b) / spread)
        if (rand.nextDouble() < e && s.add(p))
          size += 1
      }
    } while (size < n)
    s.toSet
  }
}
