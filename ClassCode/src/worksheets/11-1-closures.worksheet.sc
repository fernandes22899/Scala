import java.util.logging.Logger

// Remember how we could take a bland 2-argument
// function like this...
def addSomeToArg(n: Int, some: Int): Int = {
  def addSome(x: Int): Int = x + some
  addSome(n)
}

addSomeToArg(42, 10)

// ...and turn it into a function that yields
// a function to add a constant amount to its
// argument?
def addSome(some: Int): Int => Int = {
  def add(x: Int): Int = x + some
  add
}

val add10 = addSome(10)
val add100 = addSome(100)

add10(42)
add100(42)
add10(1)

// What's going on here is that the 10 and
// 100 values are being stored along with
// the function in what's called a closure.
// Closures are ways to save state with
// function objects.

// We can make use of this behavior when
// writing the functions we use as arguments
// or return as results. Here's an example
// where we can add logging to any function f
// that is passed in. The function returned
// counts how many times the function is called
// and also prints out information about each call
def loggingFunction[A,B](f: A => B, name: String): A => B = {
  val logger = Logger.getGlobal
  var count = 0

  def g(x: A): B = {
    count += 1
    logger.info(s"calling $name ($count) with $x")
    val y = f(x)
    logger.info(s"$name($x) = $y")
    y
  }
  g
}

def len(str: String): Int = str.length
val lenLog1 = loggingFunction(len, "length")
lenLog1("foo")
lenLog1("bar")

val lenLog2 = loggingFunction(len, "length")
lenLog2("foo")

// Other languages have started introducing closures
// as well. The HTML chapter talks about Java and
// JavaScript, but I have also used closures in
// C++, where you are allowed to explicitly declare
// which variables you want in the closures you're
// making for a given lambda function. This can be
// useful when you want to specify some processing
// that should be done once missing pieces of data
// become available, for example.