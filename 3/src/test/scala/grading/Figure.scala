package grading

object Figure {

  import quadtree.{ QuadLeaf, QuadNode, QuadTree, Space }

  // from the text of the assignment
  lazy val space = Space(15, 10)
  val A = Point(9, 9)
  val B = Point(13, 8)
  val C = Point(6, 4)
  val D = Point(10, 5)
  val E = Point(9, 3)
  val F = Point(11, 1)

  val ABCDEF = List(A, B, C, D, E, F)
  val allPoints = ABCDEF.map(p => (p, p)).toMap

  lazy val tl = QuadLeaf(2, Space(0, 6, 7, 10), Map.empty[Point, Point])
  lazy val tr = QuadLeaf(2, Space(8, 6, 15, 10), Map(A -> A, B -> B))
  lazy val bl = QuadLeaf(2, Space(0, 0, 7, 5), Map(C -> C))
  lazy val brtl = QuadLeaf(2, Space(8, 3, 11, 5), Map(D -> D, E -> E))
  lazy val brtr = QuadLeaf(2, Space(12, 3, 15, 5), Map.empty[Point, Point])
  lazy val brbl = QuadLeaf(2, Space(8, 0, 11, 2), Map(F -> F))
  lazy val brbr = QuadLeaf(2, Space(12, 0, 15, 2), Map.empty[Point, Point])
  lazy val br = QuadNode(2, Space(8, 0, 15, 5), brtl, brtr, brbl, brbr)
  lazy val quad: QuadTree[Point, Point] = QuadNode(2, Space(0, 0, 15, 10), tl, tr, bl, br)
}
