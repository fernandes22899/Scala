package quadtree

/**
  * A location in a 2D space.
  * It consists of a horizontal coordinate `x` and a vertical coordinate `y`.  For convenience,
  * these can be obtained individually or as a pair.  The main methods of this trait are
  * `distanceTo` and `around`, which are used to calculate distances and bounding boxes.  Note that
  * the semantics of these method can vary (e.g., Euclidean vs non-Euclidean geometries), which can
  * result in implementations of `Location` that are incompatible with each others (see, for
  * instance, [[[EuclideanLocation]]] and [[[LatLonLocation]]].)
  */
trait Location[Loc] {
  /** The horizontal coordinate. */
  def x: Int

  /** The vertical coordinate. */
  def y: Int

  /** Both coordinates as pair. */
  def coordinates: (Int, Int) = (x, y)

  /** The distance between this and another location. */
  def distanceTo(that: Loc): Double

  /**
    * A bounding box around this location.
    * The box extends by the same distance in both directions.
    */
  def around(r: Double): Space
}
