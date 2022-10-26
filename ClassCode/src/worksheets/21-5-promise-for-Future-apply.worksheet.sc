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
import scala.concurrent.{ Await, ExecutionContext, Future, Promise }
import scala.util.{ Failure, Success, Try }

// It is handy to have syntax to create
// Futures like the following (from a prior
// worksheet):
// val f1: Future[Unit] = Future(print5('*'))(global)
// val f2: Future[Unit] = Future {
//   print5('+')
// }
// This is actually done using the "apply"
// method of a Future object. We can recreate
// it like this:
object MyFuture {
  def apply[A](code: => A)(implicit exec: ExecutionContext): Future[A] = {
    val p: Promise[A] = Promise[A]()
    exec.execute(() => p.complete(Try(code)))
    p.future
  }
}

// Here's an example of using it...
def calc(): Int = {
  println("start calculation")
  Thread.sleep(3000)
  42
  //uncomment line below for example of how exceptions are handled.
  //throw new NullPointerException()
}

println("START")
val f: Future[Int] = MyFuture {
  calc()
}

Await.ready(f, Duration.Inf)
f.value.get match {
  case Success(v) => println(v)
  case Failure(ex) => println(s"failed: $ex")
}

global.shutdown()
println("END")