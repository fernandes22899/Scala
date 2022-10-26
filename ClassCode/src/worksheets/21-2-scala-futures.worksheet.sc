// Scala has its own futures that work with
// other scala types and methods you're now
// familiar with. isCompleted is just a Boolean
// that returns whether or not the thread is
// done. The "value" field returns an option,
// so if it is still running it returns None,
// but if it is completed it returns Some()
// with more information inside. (More coming.)
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

/** A thread pool similar to Java's CachedThreadPool, except that idle threads terminate after
 * 1 second instead of 1 minute.  It is used in demos instead of Scala's global pool because the
 * latter uses daemon threads (which leads to undesirable early termination in demos)
 */
import java.util.concurrent.{ SynchronousQueue, ThreadPoolExecutor, TimeUnit }
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

implicit lazy val global: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(
  new ThreadPoolExecutor(
    0,
    Integer.MAX_VALUE,
    1L,
    TimeUnit.SECONDS,
    new SynchronousQueue
  )
)
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ Await, Future }
import scala.util.Random

def print5(c: Char): Unit = {
  5 times {
    Thread.sleep(Random.nextInt(1000))
    println(c)
  }
}

println("START")

val f1: Future[Unit] = Future(print5('*'))(global)
val f2: Future[Unit] = Future {
  print5('+')
}

println(f1.isCompleted)
println(f2.isCompleted)

println(f1.value)
println(f2.value)

println(Await.ready(f1, 1.minute))
println(Await.result(f2, 1.minute))

println(f1.isCompleted)
println(f2.isCompleted)

println(f1.value)
println(f2.value)

global.shutdown()
println("END")

