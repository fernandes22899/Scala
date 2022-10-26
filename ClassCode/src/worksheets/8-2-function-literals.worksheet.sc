// Higher-order functions are even more useful
// when the language supports function literals
// (also known as lambdas or anonymous functions).
val grades = List(88, 91, 78, 69, 100, 98, 70)

// Here's an anonymous function that returns
// True if its only argument is greater than 90
(x: Int) => x > 90

// We can use that in our call to find
grades.find((x: Int) => x > 90)

// Lambdas can take multiple arguments
(x: Int, y: String) => y.length == x

// And sometimes the compiler can figure
// out the argument type by the context,
// so you can leave off the argument type(s)
grades.find(x => x > 90)

grades.find(g => g.toString.endsWith("0"))

// You can also store functions as values
// And they act just like functions, because
// they pretty much are--it's basically
// the same mechanism
val f1: Int => Boolean = x => x > 90

f1(10)
f1.apply(10)

grades.find(f1)

// Here's what it looks like more formally.
object f2 extends Function[Int, Boolean] {
  def apply(x: Int): Boolean = x > 90
}
grades.find(f2)

// There's more going on under the hood to
// convert the find method of this object
// into an actual function, but the conversion
// happens automatically.