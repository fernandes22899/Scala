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

import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future, Promise }
import scala.util.Try


def calc(): Int = {
  println("start calculation")
  Thread.sleep(3000)
  42
  //throw new NullPointerException()
}

// Promises are the slightly more low-level construct
// on which Futures are built. They allow you to say
// when a computation has a result using "success",
// "failure", or "complete" (which is used below).
// Remember to "wrap" any computation you do in a
// call to one of these in a Try so that exceptions
// are properly passed from thread to thread.
println("START")
val p: Promise[Int] = Promise[Int]()
val f: Future[Int] = p.future

println(f)
println(f.isCompleted)

// Note that complete is run in a separate thread
// so that the main thread can continue while
// the calculation is running, otherwise it
// pauses this thread until it is done.
//p.complete(Try(calc()))
global.execute(() => p.complete(Try(calc())))

println(f)
println(f.isCompleted)

for(n <- f)
  println(n)

println("Waiting...")
Await.ready(f, Duration.Inf)
println("Done waiting...")

println(f)
println(f.isCompleted)

global.shutdown()
println("END")
