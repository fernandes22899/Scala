// There is a difference to the compiler between
// these declarations
def method(x: Int): Boolean = x > 90

val function: Int => Boolean = x => x > 90

val v1 = function
// Following would fail because the compiler
// expects it to be called with arguments
//val v2 = method

// Both functions give the same result
val grades = List(88, 91, 78, 69, 100, 98, 70)

grades.find(function)
grades.find(method)

// But if you want to turn method into a
// function, you need to specify the _
// This is partially-applying the function.
// The underscore says "this will be filled
// in later."
method(_)

grades.find(method(_))

// Which means you can bind some arguments
// to values and leave other arguments unbound
def less(x: Int, y: Int) = x < y
val f = less(_:Int,3)
f(2)
f(5)

less _

// You can also use the underscore for the
// unnamed argument(s) in a lambda expression
val f1: Int => Boolean = _ > 90
val f2: Int => Boolean = _ * 1.1 > 90

f1(88)
f2(88)

grades.find(_ > 90)
grades.find(_ * 1.1 > 90)

// If there is more than one argument, underscores
// are resolved positionally.
val f3: (String, Int) => Boolean = _.length > _

val f4: (String, Int) => Boolean =
  (name, minLenth) => name.length > minLenth

(name: String, minLenth: Int) => name.length > minLenth

f3("foo", 3)
