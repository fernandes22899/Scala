// Before going on to the fold methods, we need
// to cover currying.
// Functional languages generally support the
// currying of functions. This is the chaining
// of one-argument functions to implement the
// call of a multi-argument function. For example,
// consider an increment method.
def incr(x: Int): Int = x+1
incr(10)

// Here's a curried form that allows you to
// pick the increment amount.
def p(n: Int)(x: Int): Int = x+n
p(2)(10)
p(2)(_)
p(2) _
val incrBy2 = p(2) _

incrBy2(10)
incrBy2(20)

// Yes, you could just do this with a 2-argument
// funtion, but with currying you can make new
// functions that look like language features
p(3) {
  val x = 42
  x * x
}

// Here's one that's more like a language feature
def repeat[A](n: Int)(code: => A): Unit = {
  require(n >= 0)
  if (n > 0) {
    code
    repeat(n-1)(code)
  }
}

repeat(3) {
  val s = "hello"
  println(s)
}

// We can use currying to write functions to
// pass as parameters, as well.
val grades = List(88, 91, 78, 69, 100, 98, 70)

def largerThan1(bound: Int): Int => Boolean = x => x > bound
grades.find(largerThan1(90))

// Note that when writing it like this, we
// can specify the first argument and result
// in a function that still requires the second
// argument.
def largerThan(bound: Int)(x: Int): Boolean = x > bound
largerThan(90)(91)

val f: Int => Boolean = largerThan(90)

grades.find(largerThan(90))

// Here's an example with 3 arguments
def between(low: Int)(high: Int)(str: String): Boolean =
  str.lengthIs >= low && str.lengthIs <= high

between(1)(5)("foo")

val f1: String => Boolean = between(1)(5)

val f2: Int => String => Boolean = between(1)

f1("foo")
f2(5)
f2(5)("foo")

f2(5) {
  "foo"
}

// Given that, here are a couple of other functions
// that take functions as 2nd inputs. Both of these
// generate new lists--tabulate basically
// transforms a list of numbers starting at
// zero up to (but not including) the length
// given, and iterate applies a function over
// and over to the most recent result to
// generate a list of the given length.
List.tabulate(10)(i => "*" * i)
List.iterate(2.0, 10)(Math.sqrt)