// We're bringing in a special println that prints out
// the name of the thread it's being run on and the time
// that something is being printed
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

import scala.util.Random

// To start a new thread of executation, we need something
// that extends Runnable, and defines the run method
// Let's define one that prints a character 5 times with
// a very small amount of time in between each character.
class Printer(c: Char) extends Runnable {
  def run(): Unit = {
    5 times {
      println(c)
      Thread.sleep(Random.nextInt(1000))
    }
  }
}

val r1 = new Printer('*')
val r2 = new Printer('+')

// Here's where we actually create the threads,
// start them, and then wait for them to finish (join)
println("START")

val t1 = new Thread(r1, "T1")
val t2 = new Thread(r2, "T2")
t1.start()
t2.start()

t1.join()
t2.join()

println("END")

// Note that every time we run this, we can end
// up with different behavior. Parallel threads
// tend to exhibit non-deterministic behavior,
// which is why it can be important to have strong
// unit tests that try multiple orderings when things
// could happen at essentially the same time.