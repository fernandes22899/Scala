// Scala for statement also supports "yield" syntax
// The "yield" generates a collection of the same type
// (when feasible), filtered and/or transformed in some way
// (rather than running code blocks for their side effects)
val l = List("Java", "C", "Scala")

val a = for (x <- l) yield x.toUpperCase
val b = for (x <- l) x.toUpperCase // useless

for {
  x <- l
  if x.length < 5
} yield "[" + x + "]"

val v = Vector("A", "B", "C")

for (x <- v) yield x.toLowerCase

val m = Map(520 -> "C", 735 -> "Java")

for ((c,l) <- m) yield c -> l.toLowerCase

for (c <- "Java") yield c.toLower

for ((c,l) <- m) yield l + " in " + c

for (c <- "Java") yield c + 10

for (i <- 1 to 10) yield 10 * i
(for (i <- 1 to 10) yield 10 * i).toList
for (i <- (1 to 10).toList) yield 10 * i
