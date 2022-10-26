import scala.util.Random

// Check out this method of List. It takes an
// integer, and a block of code to be used to
// fill each value in the list. But how does
// it get a different random letter every time,
// instead of just the one that's created when
// we first call fill? We're going to
// see how to write something like this.
List.fill(5) {
  val n = 'A' + Random.nextInt(26)
  n.toChar
}

// The syntax above is possible in part because
// of currying (as we've seen before). Here's
// an example of writing a small function that
// takes two argument lists, and we can specify
// an argument list in a block if it has just
// one item. Remember this in a few minutes.
def f(x: Int, y: Int)(s: String): String = s + x + y

f(1,2)("foo")

f(1,2) {
  "foo"
}

// The other part that makes List.fill work
// that way is the ability to pass arguments
// *by name* rather than by value or by
// reference. A type of "=> X" indicates that
// we want to pass a piece of code *unevaluated*
// and it says that code should return type X
// The function copy passes does this (note
// how it behaves different when combined with
// up() when s is just a String versus when
// it is passed by name.
def copy(s: => String): String = {
  println("start copy")
  val r = s + s
  println("end copy")
  r
}

def up(s: String): String = {
  println("start up")
  val r = s.toUpperCase
  println("end up")
  r
}

copy(up("foo"))

// Now you can see how this works to create
// a function like fill. Note that x in the
// second argument list is being passed
// by name, so it evaluates the entire block
// that was being passed every time x is
// executed, giving us a different random
// result each time. If x were only evaluated
// once, that wouldn't happen!
def fill[A](n: Int)(x: => A): List[A] = {
  val b = List.newBuilder[A]
  var i = n
  while (i > 0) {
    b += x
    i -= 1
  }
  b.result()
}

fill(5) {
  val n = 'A' + Random.nextInt(26)
  n.toChar
}
