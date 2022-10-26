// These 4 monadic functions (map, flatMap, foreach
// and withFilter) are all that is required to
// support the for/yield semantics.
val strings = List("Scala", "Java", "C")
val courses = Map(671 -> "Scala", 520 -> "C", 735 -> "Java")

val num: Option[Int]   = Some(42)
val nonum: Option[Int] = None

def decr(n: Int): Option[Int] = if (n > 0) Some(n - 1) else None

// A basic for comprehension is transformed into
// a call to foreach. Note that none of these
// transformations result in a loop for the
// Option.
for (x <- strings) println(x)
strings.foreach(x => println(x))

for (x <- num) println(x)
num.foreach(x => println(x))

for ((c,l) <- courses) println(s"$c in $l")
courses.foreach { case (c,l) => println(s"$c in $l") }

// A for/yield with nothing special in the
// parentheses (generator) is transformed
// into a call to map
for (s <- strings) yield s.toUpperCase
strings.map(s => s.toUpperCase)

for (x <- num) yield x - 1
num.map(_ - 1)

// When the generator for the for comptrehension
// has an if statement inside, it is
// transformed to a call to withFilter
// prior to the appropriate other call.
// Note that withFilter() doesn't generate an
// intermediate list like filter() does.
for (s <- strings if s.endsWith("a")) yield s.toUpperCase
strings.withFilter(s => s.endsWith("a")).map(s => s.toUpperCase)

// Finally, if you have mutltiple generators
// in your for comprehension, it is transformed
// into a call to flatMap prior to the
// appropriate other call
val nums = List(10, -3, 42)

for (n <- nums; x <- decr(n)) yield x
nums.flatMap(n => decr(n).map(x => x))
// Note that when just yielding the original
// value back, this can be simplified further.
nums.flatMap(n => decr(n))

// Here's a more general transformation
for (n <- nums; x <- decr(n)) yield x * 10
nums.flatMap(n => decr(n).map(x => x * 10))

for {
  n <- nums
  x <- decr(n)
} yield x * 10

// Note that there's also a flatten method
// that can be used to do the flattening
// without having to use flatMap to get there.
Some(Some(10)).flatten
