// Trying to share a mutable data structure between threads
// without it having been built for it can lead to disasterous
// behavior.
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }
import scala.collection.mutable.ArrayBuffer

var shared = ArrayBuffer.empty[String]

//val lock = new Object()

class Adder(n: Int, s: String) extends Runnable {
  // Don't put the lock in here
  // val lock = new Object()

  def run(): Unit = {
    n times {
      //lock.synchronized {
      shared += s
      //}
    }
  }
}

for (_ <- 1 to 20) {
  shared = ArrayBuffer.empty[String]

  val r1 = new Adder(5000, "foo")
  val r2 = new Adder(5000, "bar")

  val t1 = new Thread(r1, "T1")
  val t2 = new Thread(r2, "T2")
  t1.start()
  t2.start()

  t1.join()
  t2.join()

  println(shared.size)
}

// We can use locks in a few different ways to
// restrict the number of threads that can run a
// section of code (or a method) at one time to
// just one thread.
