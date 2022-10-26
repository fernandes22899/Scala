// Scala if-expressions return the value from one branch
// (simliar to C's ?: trinary operator,
//  but looks like Java/C if/else statments)
val a = 2
val b = 3

if (a < b)
  println("less")
else
  println("more")

println(if (a < b) "less" else "more")

// Note that you can therefore assign constants with conditionals
var arg = "-v"
val verbosity =
  if (arg == "-v") 1
  else if (arg == "-vv") 2
  else 0

val x = -10

val p = if (x > 0) {
  println("positive")
  1
}
else {
  println("negative")
  -1
}

// The else clause can be left out--assumes Unit return value ()
if (x > 0) println(x)

// Many functional languages require else to always be present

// while, do-while similar to Java/C
var sum = 0
var n = 5
while (n > 0) {
  sum += n
  n -= 1
}
sum

sum = 0
n = 5
do {
  sum += n
  n -= 1
} while (n > 0)
sum