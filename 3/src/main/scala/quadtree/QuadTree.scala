package quadtree

/**
  * A location map as a ''quadtree''.
  * Keys of type [[[Location]]] are mapped to values of type `A`.  The class offers methods for
  * spatial lookup, as well as a few higher-order functions.
  *
  * A quadtree is a tree-like structure in which internal nodes have 4 children to represent the 4
  * quadrants of a 2D space.  This structure allows for faster lookups when it can be asserted that
  * a target cannot belong to a subtree because of its spatial properties.
  *
  * This implementation relies on 2 subclasses: [[QuadNode]] and [[QuadLeaf]].
  * When a tree is in canonical form, a leaf node is guaranteed not to contain more than a given
  * number of element (the [[threshold]]).  When more elements need to be stored in a subtree,
  * it is implemented as an internal node, with 4 children.
  */
sealed abstract class QuadTree[Loc <: Location[Loc], A] {

  /**
    * The space covered by this map.  All the elements in the map have locations in this space.
    * Elements outside this space cannot be added to the map.
    */
  def space: Space

  /** The number of elements in the map. */
  def size: Int

  /** True exactly when the map contains no element. */
  def isEmpty: Boolean

  /** Simple lookup.  Returns the `A` value mapped to this location, if any. */
  def get(loc: Loc): Option[A]

  /** Map dump.  Dumps the content of this location map into a regular (immutable) Scala Map. */
  def toMap: Map[Loc, A]

  /**
    * Removes an element based on its location.  A new map is returned.
    * If the specified location is not in the space of the map, the map is returned unchanged.
    */
  def remove(loc: Loc): QuadTree[Loc, A]

  /** Removes all the elements with locations in the specified space. */
  def clearSpace(space: Space): QuadTree[Loc, A]

  /**
    * All elements in a space.
    * This retrieves all the elements from the map that are associated with locations in the given
    * space.  The elements are returned as a Map for convenience, but no guarantees are made
    * regarding the efficiency of lookups in this map.
    */
  def get(space: Space): Map[Loc, A]

  /**
    * A higher-order function that applies `f` to all the elements of the map.
    * A new map is returned with the same locations (i.e., elements are transformed but not moved.)
    */
  def map[B](f: A => B): QuadTree[Loc, B]

  /**
    * A higher-order function that removes from the map all the elements that don't satisfy the
    * given condition.  A new map is returned.
    */
  def filter(f: ((Loc, A)) => Boolean): QuadTree[Loc, A]

  /**
    * A higher-order function that applies function `f` to all the elements in the map.
    * `f` is applied for the sake of side effects and no value is returned.
    */
  def foreach[B](f: ((Loc, A)) => B): Unit

  /**
    * Threshold (or bucket size).
    * Subtrees that contain more than that many elements are implemented as internal nodes.
    * Smaller subtrees are implemented as leaves.
    */
  def threshold: Int

  /**
    * All the elements within a circle.  The circle is specified as a location and a radius.
    * The radius is in the same unit as the parameter of [[Location.around]] for this location
    * type.
    */
  def getWithin(loc: Loc, dist: Double): Map[Loc, A]

  /**
    * Adds a new pair to the map.  A new map is returned.
    * If the specified location was already in the map, it is associated with the new value.
    * If the specified location is not in the space of the map, an `IllegalArgumentException` is
    * thrown.
    */
  @throws[IllegalArgumentException]("if the given location is outside the space")
  def add(loc: Loc, elem: A): QuadTree[Loc, A]

  /**
    * Adds a new pair to the map.  The specified element is used as both the value and the key.
    */
  @throws[IllegalArgumentException]("if the given element is outside the space")
  def add[B <: A with Loc](elem: B): QuadTree[Loc, A]

  /**
    * The closest element to the given location.  The element is returned alongside its location and
    * its distance to the target.  If no element can be found, `NoSuchElementException` is thrown.
    *
    * The method takes an optional filter and will limit its search to elements that satisfy the
    * filter, if any.
    */
  @throws[NoSuchElementException]("if no element can be found")
  def getNearest(loc: Loc, filter: Option[((Loc, A)) => Boolean] = None): (Loc, A, Double)
}

/**
  * An internal node in a quadtree.
  * In addition to a space and a threshold, a `QuadNode` has 4 children (one per quadrant), which
  * are also quadtrees.
  *
  * The class is public (and has a public constructor) for testing and grading purposes.
  *
  * @param tl the subtree for the top-left quadrant
  * @param tr the subtree for the top-right quadrant
  * @para90m bl the subtree for the bottom-left quadrant
  * @param br the subtree for the bottom-right quadrant
  */
case class QuadNode[Loc <: Location[Loc], A](threshold: Int, space: Space,
    tl: QuadTree[Loc, A], tr: QuadTree[Loc, A], bl: QuadTree[Loc, A], br: QuadTree[Loc, A]
) extends QuadTree[Loc, A] {

  def size: Int = tl.size + tr.size + bl.size + br.size // add sizes of all parts of the QuadTree
  def isEmpty: Boolean = size < 1 //Do all quadrants equal to 0?
  def toMap: Map[Loc, A] = tl.toMap ++ tr.toMap ++ bl.toMap ++ br.toMap
  def get(loc: Loc): Option[A] = space.split match{ //getting specific grids of the QuadNode
    case (split,_,_,_) if split.contains(loc) => tl.get(loc)
    case (_,split,_,_) if split.contains(loc) => tr.get(loc)
    case (_,_,split,_) if split.contains(loc) => bl.get(loc)
    case (_,_,_,split) if split.contains(loc) => br.get(loc)
    case _ => None
  }//space.contains(loc)
  def get(space: Space): Map[Loc, A] = { //getting grids in a space
    if(this.space.intersects(space))
      tl.get(space) ++ tr.get(space) ++ bl.get(space) ++ br.get(space)
    else
      Map()
  }
  def getNearest(loc: Loc, filter: Option[((Loc, A)) => Boolean]): (Loc, A, Double) = ???
  def getWithin(loc: Loc, dist: Double): Map[Loc, A] = {
    val sLoc: Space = Space(Math.ceil(loc.x - dist).toInt, Math.ceil(loc.y - dist).toInt,
      Math.floor(loc.x + dist).toInt, Math.floor(loc.y + dist).toInt)
    if(this.space.intersects(sLoc))
      tl.getWithin(loc, dist) ++ tr.getWithin(loc, dist) ++ bl.getWithin(loc, dist) ++ br.getWithin(loc, dist)
    else
      Map()
  }
  def clearSpace(sp: Space): QuadTree[Loc, A] = this.filter(fil => !sp.contains(fil._1))
  def remove(loc: Loc): QuadTree[Loc, A] = ???
  def add[B <: A with Loc](elem: B): quadtree.QuadTree[Loc, A] = this.add(elem, elem)
  def add(loc: Loc, elem: A): quadtree.QuadTree[Loc, A] = ???
  def map[B](f: A => B): QuadTree[Loc, B] = QuadNode(threshold, space, tl.map(f), tr.map(f), bl.map(f), br.map(f))
  def filter(f: ((Loc, A)) => Boolean): QuadTree[Loc, A] = {
    val fil : QuadNode[Loc, A] = QuadNode(threshold, space, tl.filter(f), tr.filter(f), bl.filter(f), br.filter(f))
    if(fil.size <= size)
      QuadLeaf(threshold, space, fil.toMap)
    else
      fil
  }
  def foreach[B](f: ((Loc, A)) => B): Unit = {
    tl.foreach(f)
    tr.foreach(f)
    bl.foreach(f)
    tr.foreach(f)
  }
}

/**
  * A leaf in a quadtree.
  * In addition to a space and a threshold, a leaf has a basic map of its elements.
  *
  * The class is public (and has a public constructor) for testing and grading purposes.
  *
  * @param elems the contents of the leaf
  */
case class QuadLeaf[Loc <: Location[Loc], A](threshold: Int, space: Space, elems: Map[Loc, A]
) extends QuadTree[Loc, A] {

  def size: Int = elems.size //looking for Map[loc, A] size
  def isEmpty: Boolean = size < 1 //is size 0?
  def toMap: Map[Loc, A] = elems
  def get(loc: Loc): Option[A] = {
    if(elems.contains(loc))
      Some(elems(loc))
    else
      None
  }
  def get(space: Space): Map[Loc, A] = {
    if(this.space.intersects(space))
      elems.filter((fil => space.contains(fil._1)))
    else
      Map()
  }
  def getNearest(loc: Loc, filter: Option[((Loc, A)) => Boolean]): (Loc, A, Double) = ???
  def getWithin(loc: Loc, dist: Double): Map[Loc, A] = {
    val sLoc: Space = Space(Math.ceil(loc.x - dist).toInt, Math.ceil(loc.y - dist).toInt,
      Math.floor(loc.x + dist).toInt, Math.floor(loc.y + dist).toInt)
    if(this.space.intersects(sLoc))
      elems.filter(fil => fil._1.distanceTo(loc) <= dist)
    else
      Map()
  }
  def clearSpace(sp: Space): QuadTree[Loc, A] = this.filter(fil => !sp.contains(fil._1))
  def remove(loc: Loc): QuadTree[Loc, A] = {
    if(elems.contains(loc))
      QuadLeaf(threshold, space, elems.removed(loc))
    else
      QuadLeaf(threshold, space, elems)
  }
  def add[B <: A with Loc](elem: B): quadtree.QuadTree[Loc, A] = this.add(elem, elem)
  def add(loc: Loc, elem: A): quadtree.QuadTree[Loc, A] = ???
  def map[B](f: A => B): QuadTree[Loc, B] = ???
  def filter(f: ((Loc, A)) => Boolean): QuadTree[Loc, A] = QuadLeaf(threshold, space, elems.filter(f))
  def foreach[B](f: ((Loc, A)) => B): Unit = ???
}

/**
  * Companion object of the [[QuadTree]] class.
  */
object QuadTree {

  /**
    * Builds a new map from the specified locations and elements.
    * Throws `IllegalArgumentException` if a location is not contained in the given space.
    */
  @throws[IllegalArgumentException]("if the space does not contain all the locations")
  def apply[Loc <: Location[Loc], A](threshold: Int, space: Space, locs: Map[Loc, A]): QuadTree[Loc, A] = {
    for(num <- locs.keys){
      if(!space.contains(num))
        throw new IllegalArgumentException("Error: Not all locations found.")
    }
    if(threshold <= locs.size){
      val tl: Space = space.split._1
      val tr: Space = space.split._2
      val bl: Space = space.split._3
      val br: Space = space.split._4
      QuadNode(threshold, space, QuadTree(threshold,tl,locs.filter(num => tl.contains(num._1))),
        QuadTree(threshold, tr, locs.filter(num => tr.contains(num._1))),
        QuadTree(threshold,bl,locs.filter(num => bl.contains(num._1))),
        QuadTree(threshold,br,locs.filter(num => br.contains(num._1))))
    }
    else
      QuadLeaf(threshold,space,locs)
  }

  /**
    * Builds a new map from the specified elements.
    * The elements are used as keys ''and'' values.
    * Throws `IllegalArgumentException` if a location is not contained in the given space.
    */
  @throws[IllegalArgumentException]("if the space does not contain all the elements")
  def apply[A <: Location[A]](threshold: Int, space: Space, locs: Iterable[A]): QuadTree[A, A] = {
    for(num <- locs){
      if(!space.contains(num))
        throw new IllegalArgumentException("Error: Not all locations found.")
    }
    if(threshold <= locs.size){
      val tl: Space = space.split._1
      val tr: Space = space.split._2
      val bl: Space = space.split._3
      val br: Space = space.split._4
      QuadNode(threshold, space, QuadTree(threshold,tl,locs.filter(num => tl.contains(num))),
        QuadTree(threshold, tr, locs.filter(num => tr.contains(num))),
        QuadTree(threshold,bl,locs.filter(num => bl.contains(num))),
        QuadTree(threshold,br,locs.filter(num => br.contains(num))))
    }
    else
      QuadLeaf(threshold,space,locs.map(num => num -> num).toMap)
  }

  /**
    * Builds a new map from the specified locations and elements.
    * The map's space is defined as the bounding box of all the keys.
    * See [[Space.bounds]]
    */
  def apply[Loc <: Location[Loc], A](threshold: Int, locs: Map[Loc, A]): QuadTree[Loc, A] = {
    val space: Space = Space.bounds(locs.keys)
    for(num <- locs.keys){
      if(!space.contains(num))
        throw new IllegalArgumentException("Error: Not all locations found.")
    }
    if(threshold <= locs.size){
      val tl: Space = space.split._1
      val tr: Space = space.split._2
      val bl: Space = space.split._3
      val br: Space = space.split._4
      QuadNode(threshold, space, QuadTree(threshold,tl,locs.filter(num => tl.contains(num._1))),
        QuadTree(threshold, tr, locs.filter(num => tr.contains(num._1))),
        QuadTree(threshold,bl,locs.filter(num => bl.contains(num._1))),
        QuadTree(threshold,br,locs.filter(num => br.contains(num._1))))
    }
    else
      QuadLeaf(threshold,space,locs)
  }

  /**
    * Builds a new map from the specified elements.
    * The elements are used as keys ''and'' values.
    * The map's space is defined as the bounding box of all the elements.
    * See [[Space.bounds]]
    */
  def apply[A <: Location[A]](threshold: Int, locs: Set[A]): QuadTree[A, A] = {
    val space: Space = Space.bounds(locs)
    for(num <- locs){
      if(!space.contains(num))
        throw new IllegalArgumentException("Error: Not all locations found.")
    }
    if(threshold <= locs.size){
      val tl: Space = space.split._1
      val tr: Space = space.split._2
      val bl: Space = space.split._3
      val br: Space = space.split._4
      QuadNode(threshold, space, QuadTree(threshold,tl,locs.filter(num => tl.contains(num))),
        QuadTree(threshold, tr, locs.filter(num => tr.contains(num))),
        QuadTree(threshold,bl,locs.filter(num => bl.contains(num))),
        QuadTree(threshold,br,locs.filter(num => br.contains(num))))
    }
    else
      QuadLeaf(threshold,space,locs.map(num => num -> num).toMap)
  }
}
