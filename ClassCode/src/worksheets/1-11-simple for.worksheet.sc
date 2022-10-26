// Scala for-loops assign one item at a time from
// a collection to a loop variable
val l = List("Java", "C", "Scala")

for (s <- l) print(s + " ")

for (s <- l) {
  val r = s.reverse
  print(s + r + " ")
}

val m = Map(735 -> "Java", 520 -> "C")

// Note that you can use tuple to assign multiple loop variables at once
for ((c,l) <- m) print("(" + l + " in " + c + ") ")

// You can use if after the collection selection to filter elements
for (x <- l if x.length < 5) print(x + " ")

for {
  x <- l
  if x.length < 5
} print(x + " ")

// You can also cascade selection from collections
// This is an example of using multiple generators
// For each language that ends with a,
//    for each character in that language...
for {
  lang <- l
  if lang.endsWith("a")
  c <- lang
} print(c + " ")

// Multiple generators on the same line require semicolons
for (lang <- l if lang.endsWith("a"); c <- lang ) print(c + " ")

// You can also introduce intermediate variables, like z
val a = Seq(1,2,3)
val b = List("foo", "bar")
for (x <- a if x % 2 != 0;  y <- b;  z = x+y if y startsWith "f")
  print(z + " ")

// Ranges can be used as collections as well, giving a familiar feel
for (i <- 1 to 10) print(i + " ")

// Note that you can have multiple generators
// without filtering, too.
// (This example's result is best viewed in IntelliJ)
for {
  char <- "Java"
  shift <- -1 to 1
} {
  print(" " * (1 - shift.abs))
  println((char + shift).toChar)
}
