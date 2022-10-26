// So with currying and pass-by-name, we
// have everything we need to make functions
// that look like new language features.
// Here's an example with doTwice, which
// could be used to make a more general loop.
def doTwice[U](code: => U): Unit = {
  code
  code
}

doTwice {
  println("X")
}

// Here's one that returns how long it takes
// to run arbitrary blocks of code
def timeOf[U](code: => U): Double = {
  val start = System.nanoTime()
  code
  val end = System.nanoTime()
  (end - start) / 1e9
}

timeOf {
  Thread.sleep(1000)
  println("X")
}

// It's also how we could write a method
// like times
/*
import org.tinyscalautils.Controls.Times

3 times println("x")

3.times(println("x"))
*/
object timesOnInt {

  implicit class Int2ToTimes(val n: Int) extends AnyVal {
    def times[U](code: => U): Unit = {
      var i = n
      while (i > 0) {
        code
        i -= 1
      }
    }
  }

  3 times println("x")
  new Int2ToTimes(3).times(println("X"))
}

timesOnInt

