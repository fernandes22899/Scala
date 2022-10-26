// Recall that the following two functions are equivalent
val f = (x: Int) => x + 1
f(2)

val f1 = new Function1[Int,Int] {
  def apply(x: Int) = x+1
}
f1(2)

// Let's define a new function using Scala's PartialFunction
// trait. Partial functions implement an isDefinedAt method
// to indicate what values it is valid to call apply on them
val sqrt = new PartialFunction[Double,Double] {
  def apply(x: Double) = math.sqrt(x)
  def isDefinedAt(x: Double): Boolean = x >= 0
}

// And as you might expect...
sqrt(4)
sqrt.isDefinedAt(-4)

// ...but there's also a collect method on collections
// that makes sure only to evaluate and return values
// where the partial function is being applied
val l = List(1.0, -2.0, 3.0, 4.0)
l collect sqrt

// Collections implement the PartialFunction trait
val m: PartialFunction[String,Int] = Map("CS671" -> 19, "CS415" -> 120)
m("CS671")
m.isDefinedAt("CS520")

val courses = List("CS671", "CS520", "CS415")
courses collect m

val a: PartialFunction[Int,Double] = l
l(2)

// Partial functions can be used with \"andThen\" to compose
// with other functions... applies a to x (y = a(x))
// and then applies sqrt to y (answer = sqrt(y))
val b = a andThen sqrt
b(2)

// Here's a "negative square root" function that fills in
// the places where our sqrt function is not defined
val h: PartialFunction[Double,Double] = {
  case x if x < 0 => -math.sqrt(-x)
}
h(-4)
h.isDefinedAt(4)

// We can use orElse to determine what function to
// use when not defined.
// Note: this is composing two partial functions
// into a complete function
val c = sqrt orElse h
c(4)
c(-4)

// You can also write partial functions, using case.
// sqrt2 is nearly equivalent to sqrt above.
val sqrt2:PartialFunction[Double, Double] = {
  case (x:Double) if x >= 0 => math.sqrt(x)
}
