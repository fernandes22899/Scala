package grading

// A location that keeps track of its methods being called
case class Point(xx: Int, yy: Int) extends quadtree.EuclideanLocation[Point] {
  private var count = 0

  def touched: Int = count

  def isUntouched: Boolean = count == 0

  def reset(): Unit = count = 0

  def x: Int = {
    count += 1
    xx
  }

  def y: Int = {
    count += 1
    yy
  }
}
