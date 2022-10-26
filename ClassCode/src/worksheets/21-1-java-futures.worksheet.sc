// Futures are another way of synchronizing
// threaded computations. Java has futures
// that allow you to start processes in other
// threads and then check if they're done or
// not using isDone(). You can also get the
// results with get(), which basically returns
// null/Unit is it's still running.
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

import java.util.concurrent.{ Executors, Future => JFuture }
import scala.util.Random

val exec = Executors.newFixedThreadPool(4)

def print5(c: Char): Unit = {
  5 times {
    Thread.sleep(Random.nextInt(1000))
    println(c)
  }
}

println("START")

val f1: JFuture[Unit] = exec.submit(() => print5('*'))
val f2: JFuture[Unit] = exec.submit(() => print5('+'))

//Thread.sleep(3000)

println(f1.isDone)
println(f2.isDone)

println(f1.get())
println(f2.get())

println(f1.get())
println(f2.get())

exec.shutdown()
println("END")