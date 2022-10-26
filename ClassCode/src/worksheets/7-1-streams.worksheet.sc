import scala.annotation.tailrec
import scala.util.Random

// If you were to create a recursive function
// with no base case, it would generate a
// StackOverflowError the moment you tried to
// run it.
//def countUp(n: Int): List[Int] = n :: countUp(n + 1)

// But scala allows you to define LazyLists that
// can be specified recursively with no base case
// because list items are only calculated when
// needed (notice the use of #:: in place of ::)
def countUp(n: Int): LazyList[Int] = n #:: countUp(n + 1)

val up1 = countUp(1)
up1.head
up1(100)
up1(1000)

// LazyList has some useful methods for generating
// simple lazy lists, including from, continually,
// and take
LazyList.from(1)

val a = LazyList.continually("A")
a.take(5).toList

val r = LazyList.continually(Random.nextFloat())
r.take(5).toList

// Here's an example of how getAt could be defined.
// Note that LazyList() is needed for an empty
// LazyList, rather than Nil. Also note that it
// is expected to use the #:: cons operator in
// the match, in place of ::
@tailrec
def getAt[A](stream: LazyList[A], i: Int): A = (stream, i) match {
  case (LazyList(), _) => throw new NoSuchElementException()
  case (h #:: _, 0) => h
  case (_ #:: t, _) => getAt(t, i -1)
}

getAt(up1, 1000)

// If your LazyList has side-effects, it is important
// to realize that only the values are kept--when you
// go back for a value that's already been evaluated,
// the code that generated the value won't be run
// again.
var n = 0
val s = LazyList.continually {
  println(n)
  n += 1
  n
}

s(5)
s(7)
s(4)
