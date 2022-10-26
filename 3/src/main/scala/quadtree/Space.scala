package quadtree

import scala.collection.View.{Empty, empty}

/**
  * A 2D space.
  * It is defined by its bottom-left location `(minX,minY)` and its top-right location `(maxX,maxY)`.
  * Space implementations should ensure that instances that represent the same space
  * (same bottom-left and top-right locations) are equal (e.g., by using case classes).
  */
trait Space { // CANNOT BE MODIFIED


  /** Horizontal coordinate of the bottom-left corner. */
  def minX: Int

  /** Vertical coordinate of the bottom-left corner. */
  def minY: Int

  /** Horizontal coordinate of the top-right corner. */
  def maxX: Int

  /** Vertical coordinate of the top-right corner. */
  def maxY: Int

  /**
    * Emptiness.  Note that the empty space is not unique. Spaces `(5,10,20,3)` and `(10,5,7,7)`
    * are both empty but are not equal.  In particular, they don't have the same center point.
    */
  def isEmpty: Boolean //y_Min > y_Max || x_Min > x_Max

  /**
    * Tests if a location belongs to the space.
    * The boundary of a space belongs to the space, so location `(0,1)` belongs to space `(0,0,1,1)`.
    */
  def contains(loc: Location[_]): Boolean = contains(loc.x, loc.y)

  /**
    * Tests if a location belongs to the space.
    * The boundary of a space belongs to the space, so location `(0,1)` belongs to space `(0,0,1,1)`.
    */
  def contains(x: Int, y: Int): Boolean

  /**
    * The center point of a space.
    * Because spaces are specified using integers, this point cannot always be exactly in the center
    * of the space.  Non-integers numbers are rounded down, placing this point left and below of the
    * exact center.  Note also that the center point does not belong to the space if the space is
    * empty.
    */
  def center: (Int, Int)// = centerHelp(x_Min, x_Max, y_Min, y_Max)
  //def centerHelp(xmin: Int, xmax: Int, ymin: Int, ymax: Int) : (Int, Int) = {
   // (Math.floor((xmin + xmax)/2).toInt, Math.floor((ymin + ymax)/2).toInt)
  //}
  /**
    * Overlapping test.
    * True if this space overlaps with that space.  Spaces can overlap by just their boundary.
    * Empty spaces do not overlap with any space, empty or non empty.
    */
  def intersects(that: Space): Boolean

  /**
    * Splits the space into 4 quadrants.
    * If the space is empty, all the quadrants are empty.  Otherwise, the center of the space is
    * used for the top-right corner of the bottom-left quadrant, which fully defines all quadrants.
    * The quadrants are returned as an ordered tuple: `(tl, tr, bl, br)`.
    */
  def split: (Space, Space, Space, Space)
}

/**
  * "and you need to create a class that implements Space (like SpaceImpl)" - Professor from Discord
  * @param x_min
  * @param y_min
  * @param x_max
  * @param y_max
  */
case class SpaceImpl( x_min: Int , y_min : Int , x_max : Int, y_max : Int ) extends Space {


  /** Horizontal coordinate of the bottom-left corner. */
  def minX: Int = x_min

  /** Vertical coordinate of the bottom-left corner. */
  def minY: Int = y_min

  /** Horizontal coordinate of the top-right corner. */
  def maxX: Int = x_max

  /** Vertical coordinate of the top-right corner. */
  def maxY: Int = y_max

  def isEmpty: Boolean = minY > maxY || minX > maxX

  /**
    * Tests if a location belongs to the space.
    * The boundary of a space belongs to the space, so location `(0,1)` belongs to space `(0,0,1,1)`.
    */
  def contains(x: Int, y: Int): Boolean = {
    Range( minX, maxX + 1 ).contains(x) && Range( minY, maxY + 1 ).contains(y)
  }

  def center: (Int, Int) = centerHelp(minX, maxX, minY, maxY) //need all max's and min's to determine center
  def centerHelp(xmin: Int, xmax: Int, ymin: Int, ymax: Int) : (Int, Int) = {
    (Math.floor((xmin + xmax) / 2).toInt, Math.floor((ymin + ymax) / 2).toInt)
  }

  //checking is the space overlaps
  def intersects(that: Space): Boolean = if( maxX < that.minX || minX > that.maxX
    || maxY < that.minY || minY > that.maxY ) false else true

  /**
    * Splits the space into 4 quadrants.
    * If the space is empty, all the quadrants are empty.  Otherwise, the center of the space is
    * used for the top-right corner of the bottom-left quadrant, which fully defines all quadrants.
    * The quadrants are returned as an ordered tuple: `(tl, tr, bl, br)`.
    */
  def split: (Space, Space, Space, Space) = {
    val tl: Space = Space(minX, center._2 + 1, center._1, maxY)
    val tr: Space = Space(center._1 + 1, center._2 + 1, maxX, maxY)
    val bl: Space = Space(minX,minY,center._1,center._2)
    val br: Space = Space(center._1 + 1, minY, maxX, center._2)
    (tl,tr,bl,br)
  }
}
/**
  * Companion object of the `Space` trait.
  */
object Space {

  /** Builds the space `(0,0,maxX,maxY)`. */
  def apply(maxX: Int, maxY: Int): Space = SpaceImpl(0,0,maxX, maxY)

  /** Builds the space `(minX,minY,maxX,maxY)`. */
  def apply(minX: Int, minY: Int, maxX: Int, maxY: Int): Space = SpaceImpl(minX,minY,maxX, maxY)

  /**
    * Builds the smallest space that contains all the locations (bounding box).
    * If no location is specified, the method returns an (unspecified) empty space.
    */
  def bounds(locs: Iterable[Location[_]]): Space = {
    val loc = locs.toList
    val x = for(num <- loc) yield num.coordinates._1
    val y = for(num <- loc) yield num.coordinates._2

    if(locs.isEmpty)
      Space.bounds(empty)
    else
      Space(x.min,y.min,x.max,y.max)
  }
}
