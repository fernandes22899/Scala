abstract class IntSet {
  def isEmpty: Boolean
  def size: Int
  def height: Int
  def smallest: Int

  def contains(x: Int): Boolean

  def + (x: Int): IntSet
  def - (x: Int): IntSet

  //private [intset]
  def highestRemoved: (Int, IntSet)

  def find(test: Int => Boolean): Option[Int]
  def foreach[U](f: Int => U): Unit
  def fold[A](init: A, f: (A,Int) => A): A

  def toList: List[Int] = toList0(Nil)

  //private[intset]
  def toList0(acc: List[Int]): List[Int]
}

case class Node(value: Int, lower: IntSet, higher: IntSet) extends IntSet {
  def isEmpty = false
  def size: Int = 1 + lower.size + higher.size
  def height: Int = 1 + lower.height.max(higher.height)
  def smallest: Int = if (lower.isEmpty) value else lower.smallest

  def contains(x: Int): Boolean = {
    if (x < value) lower.contains(x)
    else if (x > value) higher.contains(x)
    else true
  }

  def + (x: Int): IntSet = {
    if (x < value) Node(value, lower + x, higher)
    else if (x > value) Node(value, lower, higher + x)
    else this
  }

  def - (x: Int): IntSet = {
    if (x < value) Node(value, lower - x, higher)
    else if (x > value) Node(value, lower, higher - x)
    else if (lower.isEmpty) higher
    else if (higher.isEmpty) lower
    else {
      val (v, t) = lower.highestRemoved
      Node(v, t, higher)
    }
  }

  def highestRemoved: (Int, IntSet) =
    if (higher.isEmpty) (value, lower)
    else {
      val (v, t) = higher.highestRemoved
      (v, Node(value, lower, t))
    }

  def find(test: Int => Boolean): Option[Int] =
    if (test(value)) Some(value)
    else lower.find(test).orElse(higher.find(test))

  def foreach[U](f: Int => U): Unit = {
    lower.foreach(f)
    f(value)
    higher.foreach(f)
  }

  def fold[A](init: A, f: (A,Int) => A): A = higher.fold(f(lower.fold(init, f), value), f)

  // BAD
  //def toList: List[Int] = (lower.toList :+ value) ::: higher.toList

  //private[intset]
  def toList0(acc: List[Int]): List[Int] =
    lower.toList0(value :: higher.toList0(acc))
}

case object Empty extends IntSet {
  def isEmpty = true
  def size = 0
  def height = 0
  def smallest: Int = throw new NoSuchElementException("Empty.smallest")

  def contains(x: Int) = false

  def + (x: Int): IntSet = Node(x, Empty, Empty)
  def - (x: Int): IntSet = this

  def highestRemoved: (Int, IntSet) = throw new AssertionError("never called")

  def find(test: Int => Boolean): Option[Int] = None
  def foreach[U](f: Int => U) = ()
  def fold[A](init: A, f: (A,Int) => A): A = init

  //private[intset]
  def toList0(acc: List[Int]): List[Int] = acc
}

val set = Node(4,
  Node(2,
    Node(1, Empty, Empty),
    Node(3, Empty, Empty)
  ),
  Node(6,
    Node(5, Empty,Empty),
    Empty
  )
)

println(set.size)
println(set.height)
println(set.smallest)
set.foreach(println)
for (x <- set) println(x)
println(set.toList)
println(set - 4)
println(set.fold(0, (a: Int,x) => a * 10 + x))