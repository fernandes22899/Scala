// Expressions, inferred semicolon
2 + 2

2 + 2 +
  3

2 + 2
+ 3

(2 + 2
  + 3)

// *Every* Scala expression denotes a object of some type
2.toDouble
true.hashCode()

// Note that you don't need parentheses if no arguments
true.hashCode
2.toString
2.max(10)

// Creating "constants"--named immutable expressions
// can use syntax <identifier>: <type>
val b: Int = 2 + 2
//b = 5 //<-- would be an error at this point
val c: Double = 20 / 3.0

// Scala can infer the type in many cases
val a = 2 + 2
val s = "foo"

// names act as values/objects in expressions
b.max(3)
s.toUpperCase

val x = a + 1
a
x

// mutable variables use "var"
var y = 10
y = y + 2
y

y += 100
y
//y++ //<-- would be an error (Scala has no ++ or --)

//Note: assignment has type Unit (similar to void)
var check: Unit = (y = 52)
check = ()
