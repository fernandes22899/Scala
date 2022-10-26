// Rather than use a busy loop or waiting for a fixed
// time period, you should be using synchronizers.
// Latches and semaphores are examples.
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ThreadTimeMode, println}

import java.util.concurrent.{CountDownLatch, Executors, Semaphore}
import scala.util.Random


val exec = Executors.newFixedThreadPool(4)

//val semaphore = new Semaphore(0)
val latch = new CountDownLatch(2)

class Printer(c: Char) extends Runnable {
  def run(): Unit = {
    5 times {
      println(c)
      Thread.sleep(Random.nextInt(1000))
    }
//    semaphore.release(1)
    latch.countDown()
  }
}

val r1 = new Printer('*')
val r2 = new Printer('+')

println("START")
exec.execute(r1)
exec.execute(r2)

//semaphore.acquire(2)
latch.await()
println("END")

exec.shutdown()
