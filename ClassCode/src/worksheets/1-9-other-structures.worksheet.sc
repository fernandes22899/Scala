// Tuples--nameless, immutable structures that can hold various types at once
val a = ("foo", 42)
val b = ("foo", 2.1, true)

// Accessible by position using underscore notation, up to 22 pieces
a._1
a._2
b._3

// You can create multiple variables and assign them this way
val (s, n) = a

// Which is useful in pattern matching
val v = b match {
  case (s, _, true) => s.toUpperCase
  case (s, _, false) => s
}

// If you want to make a copy where you change a part, use copy method
val c = a.copy(_1 = "bar")
c
val d = a.swap
d

a
b

def dup(s: String): (String, String) = (s, s)

// Options--Similar to "nullable" values, but have None instead of null
val x: Option[Int] = Some(42)
val y: Option[Int] = None

// Some useful methods
x.isEmpty
y.isEmpty

x.get
//y.get

def printIt1(it: Option[Any]) = if (it.nonEmpty) println(it.get)

// But often you'd use match instead, like this
def printIt(it: Option[Any]) = it match {
  case Some(value) => println(value)
  case None => ()
}

printIt(x)
printIt(y)

// Ranges (To construct, use methods to/until and by)
// Note difference between to (inclusive) and until (exclusive)
1 to 10
1 to 19 by 3
1 until 19 by 3
10 to 1 by -2

//numeric separators don't work in most Scala versions
//val r = 0 until 1_000_000
val r = 0 until 1000000
r.head
r.last
r.length
r(10000)

// You'll see ranges most often in for loops
// Note that I'm also using a string trick here to evaluate x
// s"..." with ${<expr>} in the quotes evaluates <expr> for output
for (x <- 10 to 1 by -2) print(s"-${x}- ")

