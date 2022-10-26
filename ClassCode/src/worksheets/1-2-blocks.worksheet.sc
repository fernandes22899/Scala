// Curly braces can be used to build expressions
val a = 2 + 2
val b = {
  2 + 2
}
val c = {
  println("foo")
  2 + 2
}
val d: Int = {
  val two = 2
  two + two
}
val PiSquare = {
  import math.Pi
  Pi * Pi
}
c
// Last expression in the block is the value of the block
val e = {
  2 + 2
  println("foo")
}
val n = {
  val x = 10
  x + x
  x * x
} + {
  val x = "foo"
  x.length
}
