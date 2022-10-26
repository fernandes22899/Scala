import scala.annotation.tailrec

// Now on to important methods that take more general
// functions as arguments
val nums = List(2, 3, 1, 33, 2, 5, 42, 101, 1, 3)

// foreach takes a collection element and is not
// expected to return anything. Here are some
// equivalent statements so you can see how the
// shorthand versions of the argument work
nums.foreach(x => println(x))
nums.foreach(println(_))
nums.foreach(println)

// How might we write foreach?
@tailrec
def foreach[A,U](list: List[A], f: A => U): Unit = list match {
  case Nil => ()
  case h :: t =>
    f(h)
    foreach(t, f)
}

foreach(nums, println)

// map converts a collection element, which
// requires a function argument that converts
// one element to another (possibly of a
// different type, but could be the same type)
nums.map(x => x * 10)
nums.map("*" + _)

// How might we write map?
def map[A,B](list: List[A], f: A => B): List[B] = list match {
  case Nil => Nil
  case h :: t => f(h) :: map(t, f)
}

map(nums, (x: Int) => x * 10)

// sorted takes a predicate that takes
// two values to compare. We won't try to write
// it ourselves, just see how it can be used.
nums.sorted
nums.sorted(Ordering.Int)
nums.sortWith(_ > _)

case class Horse(height: Double)
val horses = List(Horse(12.3), Horse(13.2), Horse(12.1))

horses.sortWith(_.height < _.height)
horses.sortBy(_.height)

