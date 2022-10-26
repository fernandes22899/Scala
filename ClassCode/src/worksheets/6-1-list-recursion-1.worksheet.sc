// Lists are a natural recursive structure, so we're
// going to do a number of examples with them.
// When the last step of a recursive method is the
// recursive call, it is tail recursive, and it can
// be optimized automatically by the compiler into
// an efficient (iterative) loop
import scala.annotation.tailrec

val l = List(1, 2, 3, 4, 5)

// For example, look at this implementation of list sum.
// It may look like the last thing that happens is the
// call to sum(), but the result is needed for the
// addition, which is what really happens last.
l.sum

def sum(l: List[Int]): Int = l match {
  case Nil => 0
  case h :: t => h + sum(t)
}

sum(l)

// ...and this implementation of list length is similar
l.length

def len[A](l: List[A]): Int = l match {
  case Nil => 0
  case _ :: t => 1 + len(t)
}

len(l)

val big = List.range(0, 100000)
big.length

// The call below causes a  stack overflow
//len(big)

// Here's a common pattern for making a method tail recursive.
// Use a helper function with an additional argument
// that accumulates a partial result on the way "down"
// (before the recursive call is made), allowing the
// recursion to be the very last thing.
// Notice that there's no combining on the way back "up",
// It just returns the value returned to it.
def len2[A](l: List[A]): Int = {
  @tailrec
  def addLen(list: List[A], n: Int): Int = list match {
    case Nil => n
    case _ :: t => addLen(t, n + 1)
  }
  addLen(l, 0)
}

len2(l)
len2(big)

//sum(big)

// Here's doing the same for sum()
def sum2(l: List[Int]): Int = {
  @tailrec
  def addSum(list: List[Int], s: Int): Int = list match {
    case Nil => s
    case h :: t => addSum(t, s + h)
  }
  addSum(l, 0)
}

sum2(big)

// Let's look at how last() might be implemented
l.last

// It's not always necessary to have a helper function.
// Here's a case where just returning what we have is
// fine. (Note: needs to be "final" (or private) so that
// it's not possible to override it if it is a method of
// a class.)
@tailrec
final def last[A](l: List[A]): A = l match {
  case Nil => throw new NoSuchElementException("last(Nil")
  case x :: Nil => x
  // or case List(x) => x
  case _ :: t => last(t)
}

last(l)

// And init() is like the opposite of tail()
l.init

// Is this tail recursive?
def init[A](l: List[A]): List[A] = l match {
  case Nil => throw new NoSuchElementException("init(Nil")
  case List(x) => Nil
  case h :: t => h :: init(t)
}

init(l)

// Here's a way to make factorial tail recursive
def factorial(n: Int): Int = {
  @tailrec
  def loop(n: Int, f: Int): Int =
    if (n == 0) f else loop(n - 1, n * f)
  loop(n, 1)
}

factorial(10)
