import scala.annotation.tailrec

// from last time...
def append[A](l1: List[A], l2: List[A]): List[A] = l1 match {
  case Nil => l2
  case h1 :: t1 => h1 :: append(t1, l2)
}

val v = List(List(1,2,3), Nil, List(5), List(10,20))
v.flatten

// So we'd like to go from
//   List(List(1, 2, 3), List(), List(5), List(10, 20))
//     to
//   List(1, 2, 3, 5, 10, 20)
// basically appending lists to each other as we go
// So let's break it down more than we have before.
// If we pull the list apart, we end up with
//   List(1, 2, 3)
//     and
//   List(List(), List(5), List(10, 20))
// If that tail part were run through flatten,
// we would expect to end up with
//   List(5, 10, 20)
// How can we write the recursive step to combine
// List(1, 2, 3) with List(5, 10, 20)?

def flatten[A](list: List[List[A]]): List[A] = list match {
  case Nil => Nil
  case first :: more => append(first, flatten(more))
}

flatten(v)
v(0) ::: v(1) ::: v(2) ::: v(3)

// Be careful that you use efficient operations
// during the combination step. How might you
// go about efficiently reversing a list like
// List(1, 2, 3, 3, 4, 5, 3)

// This one prepends a list to a value (slow)
def badReverse[A](list: List[A]): List[A] = list match {
  case Nil => list
  case h :: t => badReverse(t) ::: List(h)
}

// This one uses the efficient prepend of a
// single element to the front of a list.
def reverse[A](list: List[A]): List[A] = {
  @tailrec
  def dump(from: List[A], to: List[A]): List[A] = from match {
    case Nil => to
    case h :: t => dump(t, h :: to)
  }
  dump(list, Nil)
}

val nums = List(1, 2, 3, 3, 4, 5, 3)
val big = List.range(0, 3000)

println(badReverse(nums))
println(reverse(nums))
import org.tinyscalautils.Timing.timeIt
val (r1, t1) = timeIt(badReverse(big))
assert(r1.last == big.head)
println(f"${t1 * 1000.0}%.1f")
val (r2, t2) = timeIt(reverse(big))
assert(r2.last == big.head)
println(f"${t2 * 1000.0}%.1f")

// Try doing it out the same way with this one
// We'll write our own version of "apply" called getAt
val letters = List('A', 'B', 'C', 'X', 'Y', 'Z')

letters(1)
letters.apply(1)

// So here if we pull things apart, we start with
//   list = List(A, B, C, X, Y, Z) with i = 1
// Broken up, we get
//   A   and   List(B, C, X, Y, Z)
// If A isn't the one we want, what do we do with it an i?
// What should the recursive call look like on the tail
// to get the answer we're looking for?
@tailrec
def getAt[A](list: List[A], i: Int): A = (list, i) match {
  case (Nil, _) => throw new NoSuchElementException("getAt(empty")
  case (h ::_, 0) => h
  case (_ :: t, _) => getAt(t, i -1)
}

getAt(letters, 1)
