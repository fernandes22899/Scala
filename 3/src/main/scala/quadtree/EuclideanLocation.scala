package quadtree

/**
  * A Euclidean location.
  * Distance is Euclidean distance and the space around `(x,y)` with distance `r` is the smallest
  * space that contains `(x-r,y-r)` and `(x+r,y+r)`.
  */
trait EuclideanLocation[Loc <: Location[Loc]] extends Location[Loc] {

  def distanceTo(that: Loc): Double = {
    val xx: Double = this.x - that.x
    val yy: Double = this.y - that.y
    math.sqrt(xx * xx + yy * yy)
  }

  def around(r: Double): Space = {
    val d = r.ceil.toInt
    Space(x - d, y - d, x + d, y + d)
  }
}
