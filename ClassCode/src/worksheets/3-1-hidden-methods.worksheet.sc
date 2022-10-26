// Scala has no break or continue for exiting loops. This is
// because the normal ways you would implement repetition don't
// need these (see course notes).

// Scala does a lot of implicit transformations of your code.
// One thing it does is implicit conversions (such as the
// conversion of String to RichString, or Int to RichInt as
// shown in the course notes).

// Another kind of transformation that Scala supports is
// "hidden" methods. These are methods that are called
// by certain syntactic idioms (ways of writing things).
// This is basically equivalent to overloading operators in C++
// but you do it by implementing certain named methods
import scala.collection.mutable

val a = Array("A", "B", "C")
val m = mutable.Map(520 -> "C", 671 -> "Scala")
val l = List(10, 20, 30)

// Parentheses after an object call the apply method
a(1)
a.apply(1)

l(2)
l.apply(2)

m(520)
m.apply(520)

// Here's an example with some singleton objects
// Note that in this case these are factories for making ojbects
// of the companion class.
List.apply(10, 20, 30)
Array.apply("A", "B", "C")

// Exp shows how you can "overload" the parentheses yourself
// by implementing the apply method.
class Exp(base: Int) {
  def apply(n: Int): Int = {
    var r = 1
    var i = n
    while (i > 0) {
      i -= 1
      r *= base
    }
    r
  }
}

val exp2 = new Exp(2)

exp2(10)

// The use of "() =" can also be "overloaded" by implementing
// the "update" method.
// Note that VS code updates the results of modifications
// prematurely (a2 should start off with "A", "B", "C", not "CCC")
val a2 = a.clone
a2(2) = "CCC"
a2

val a3 = a2.clone
a3.update(2, "DDD")
a3
