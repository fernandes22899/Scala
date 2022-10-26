// When working on shared mutable data, behavior
// is not always exactly what you would expect, even
// with operations you might think would be atomic!
// Notice how this function doesn't always return 10
// after being run multiple times
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

var shared: Int = 0

class Adder(n: Int) extends Runnable {
  def run(): Unit = {
    n times {
      //Thread.sleep(scala.util.Random.nextLong(20))
      shared += 1
    }
  }
}

for (_ <- 1 to 20) {
  shared = 0

  val r1 = new Adder(5)
  val r2 = new Adder(5)

  val t1 = new Thread(r1, "T1")
  val t2 = new Thread(r2, "T2")
  t1.start()
  t2.start()

  t1.join()
  t2.join()

  println(shared)
}

// This happens because += involves multiple steps:
// read, increment, and store. If both read the value
// before either increment it, one of the increment
// operations will be "lost".
