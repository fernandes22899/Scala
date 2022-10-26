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
import scala.util.{ Failure, Success, Try }

// Here is an example of how a map function could be written
// that takes a future and a function that maps its result
// to a new value. Note that it would return immediately
// with a Future for the result. Also note that it is
// working with a single object, not a whole collection.
def map[A,B](fut: Future[A], f: A => B): Future[B] = {
  val p = Promise[B]()
  fut.onComplete {
    case Success(a) => p.complete(Try(f(a)))
    case Failure(ex) => p.failure(ex)
  }
  p.future
}

println("START")

val f1: Future[Int] = Future {
  println("CALCULATING...")
  Thread.sleep(3000)
  42
}

// Note that when we start the future, it
// says the "CALCULATING..." from inside
// our initial future (that pauses for 3
// seconds afterward).
val f2: Future[String] = map(f1, (x: Int) => s"COMPLETE [$x]")

// Nothing happens with COMPLETE until the
// time is up...
f2.foreach(println)

println("Waiting...")
Await.ready(f2, Duration.Inf)
println("Done waiting...")

global.shutdown()
println("END")