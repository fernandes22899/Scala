import scala.util.Random

// Iterators tell you if there's a next item,
// and they can give you the next item and
// advance.
val i1: Iterator[Int] = List(1, 2, 3).iterator

i1.hasNext
i1.next()
i1.hasNext
i1.next()

// You can create them directly, telling them
// what to start with, and how to advance.
val i2: Iterator[Int] = Iterator.iterate(0)(x => x + 1)

i2.next()
i2.next()
i2.next()
i2.next()

// You can also use the collection & monadic operations
// on them that we talked about recently
val i3: Iterator[Int] = Iterator.iterate(0)(x => x + 1)
val i4: Iterator[String] =
  i3.map(_.toString).filter(_.length > 1).map(_.toUpperCase)

i4.next()

// Note that while these have size, they use
// next() to determine the size, and so are
// useless afterward!
val i5: Iterator[Int] = List(1, 2, 3).iterator
i5.size
i5.hasNext

// https://en.wikipedia.org/wiki/Collatz_conjecture
// This series of numbers is conjectured to stop
// eventually for any input, but it is not proven.
def collatz(num: BigInt): Unit = {
  var n = num
  println(n)
  while (n > 1) {
    n = if (n % 2 == 0) n / 2 else 3 * n + 1
    println(n)
  }
}

collatz(BigInt(31))

// We can create our own iterator by extending
// the Iterator class and providing implementations
// for hasNext and next.
class Collatz(private var n: BigInt) extends Iterator[BigInt] {
  def hasNext: Boolean = n > 1
  def next(): BigInt = {
    val m = n
    n = if (n % 2 == 0) n / 2 else 3 * n + 1
    m
  }
}

val c1 = new Collatz(BigInt(31))
c1.foreach(println)

val c2 = new Collatz(BigInt(1024,Random))
for (x <- c2) println(x)

// We could also create our own random number
// iterator this way.
class Rands extends Iterator[Float] {
  def hasNext: Boolean = true
  def next: Float = Random.nextFloat()
}

val i = new Rands
i.take(10).toList

// But it's easy to make this way as well
val r = Iterator.continually(Random.nextFloat())
r.take(10).toList