// It is more common to use thread pools than individual
// threads. The code below shows how to create one and
// use it with our Printer Runnable.
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

import java.util.concurrent.Executors
import scala.util.Random


val exec = Executors.newFixedThreadPool(4)

var finished = 0
object sync;

class Printer(c: Char) extends Runnable {
  def run(): Unit = {
    5 times {
      println(c)
      Thread.sleep(Random.nextInt(1000))
    }
    sync.synchronized {
      finished += 1
    }
  }
}

val r1 = new Printer('*')
val r2 = new Printer('+')

println("START")
exec.execute(r1)
exec.execute(r2)

// If we don't wait, these threads may not
// get much of a chance to run before the
// main thread completes and kills off the
// threads. So we need to wait, but not
// with a call to sleep (how long should
// we wait)? Or with busy waiting (resource
// intensive).

// DON'T DO THAT!
// Thread.sleep(3000)

// DON'T DO THAT!

var done = false
var n = 0
while (!done) {
  n += 1
  done = sync.synchronized(finished == 2)
}

println(n)
println("END")

exec.shutdown()
