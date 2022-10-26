package grading

import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit.NANOSECONDS

import edu.unh.cs.mc.utils.threads.Executors.{ newUnlimitedThreadPool, withLocalContext }
import edu.unh.cs.mc.utils.threads.{ KeepThreads, TestThreadFactory, timeFuture }
import org.scalatest.Suite
import org.scalatest.time.SpanSugar._

import scala.concurrent.{ Await, ExecutionContext, Future }

trait TimingTests { self: Suite =>

  import parallel.{ Dispatcher, PoolWork, ThreadWork }

  // Some JVM implementations don't seem to sleep quite enough
  private val delay = (d: Double) => {
    val deadline = d.seconds.fromNow
    while (deadline.hasTimeLeft())
      NANOSECONDS.sleep(deadline.timeLeft.toNanos)
    Thread.currentThread
  }

  /**
    * A simulated application used to test `Dispatcher`, `ThreadWork` and `PoolWork`.
    *
    * @see [[ThreadWork]]
    * @see [[PoolWork]]
    * @see [[Dispatcher]]
    *
    * @param bound an optional number of permits for a semaphore in the dispatcher.
    * @param jobs  a list of tasks represented as their desired duration in seconds
    *              (and implemented as calls to `sleep`).
    */
  class TimingTest(bound: Option[Int], jobs: Seq[Double]) {

    /**
      * Runs the jobs using `ThreadWork`.
      *
      * @param workers the number of threads.
      * @param tf      a thread factory.
      * @return the duration of the computation (in seconds) and the list of names of the threads
      *         that completed the jobs.
      */
    def runThreads(workers: Int)(implicit tf: ThreadFactory): Future[(Double, List[Thread])] = {
      val dispatcher = new Dispatcher[Double, Thread](jobs.iterator, bound)
      val work = new ThreadWork(workers, delay, dispatcher)
      test(work.start(), dispatcher)
    }

    /**
      * Runs the jobs using `PoolWork`
      *
      * @param exec a thread pool.
      * @return the duration of the computation (in seconds) and the list of the threads
      *         that completed the jobs.
      */
    def runPool(implicit exec: ExecutionContext): Future[(Double, List[Thread])] = {
      val dispatcher = new Dispatcher[Double, Thread](jobs.iterator, bound)
      val work = new PoolWork(delay, dispatcher)
      test(work.start(), dispatcher)
    }

    private def test(start: => Unit, dispatcher: Dispatcher[_, Thread]) = {
      import scala.concurrent.ExecutionContext.Implicits.global
      timeFuture {
        start // must be included in timing since it's starting working threads
        dispatcher.future
      }
    }
  }

  def testTiming(
    times:    Seq[Double],
    threads:  Option[Int],
    bound:    Option[Int],
    expected: Int
  ): Unit = {
    val tf = new TestThreadFactory with KeepThreads

    withLocalContext(newUnlimitedThreadPool()(tf)) { implicit exec =>
      val timing = new TimingTest(bound, times)
      val f = {
        threads match {
          case Some(n) => timing.runThreads(n)(tf)
          case None    => timing.runPool(exec)
        }
      } map {
        case (t, l) =>
          assert(l.length === times.length)
          assert(l.toSet.subsetOf(tf.allThreads.toSet))
          assert(Math.floor(t) === expected)
      }
      Await.ready(f, (expected + 1).seconds)
    }
  }
}
