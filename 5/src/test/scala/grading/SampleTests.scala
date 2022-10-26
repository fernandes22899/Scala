package grading

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

import edu.unh.cs.mc.Implicits.futureAssertions
import edu.unh.cs.mc.utils.threads.Executors.{ newThreadPool, withLocalContext }
import edu.unh.cs.mc.utils.threads.{ KeepThreads, TestThreadFactory }
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow

import scala.concurrent._
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Using

class SampleTests extends AnyFunSuite with TimingTests with TimeLimits {

  import parallel._
  import parallel.GrepApp.TextSource

  test("single thread, no semaphore") {
    val range = 1 to 100
    val disp = new Dispatcher[Int, Int](range.iterator)
    for (i <- range) {
      val w = disp.next()
      assert(w.contains(i))
      assert(!disp.future.isCompleted)
      assert(disp.report(i) === (i == 100))
    }
    assert(disp.next().isEmpty)
    assert(disp.future.isCompleted)
    assert(disp.future.value.get.get === range)
  }

  test("single thread, semaphore") {
    val range = 1 to 100
    val disp = new Dispatcher[Int, Int](range.iterator, Some(17))
    for (i <- range) {
      val w = disp.next()
      assert(w.contains(i))
      assert(!disp.future.isCompleted)
      assert(disp.report(i) === (i == 100))
    }
    assert(disp.next().isEmpty)
    assert(disp.future.isCompleted)
    assert(disp.future.value.get.get === range)
  }

  test("handout, threads", Slow) {
    implicit val tf = new TestThreadFactory with KeepThreads

    def work(n: Int) = {
      Thread.sleep(n * 1000)
      Thread.currentThread -> n
    }

    val dispatcher = new Dispatcher[Int, (Thread, Int)](Iterator(2, 1, 5, 3, 4), None)

    val workers = new ThreadWork(4, work, dispatcher)
    failAfter(1.second)(workers.start())
    assert(!dispatcher.future.isCompleted)
    val (threads, values) = Await.result(dispatcher.future, 6.seconds).unzip // should take about 5 seconds
    assert(values.length === 5)
    assert(threads.toSet === tf.allThreads.toSet)
    assert(values === List(1, 2, 3, 4, 5) || values === List(1, 2, 3, 5, 4))
    assert(tf.allThreads.length === 4)
  }

  test("handout, pool", Slow) {
    implicit val tf = new TestThreadFactory with KeepThreads

    def work(n: Int) = {
      Thread.sleep(n * 1000)
      Thread.currentThread -> n
    }

    val dispatcher = new Dispatcher[Int, (Thread, Int)](Iterator(2, 1, 5, 3, 4), None)

    withLocalContext(newThreadPool(4)) { implicit exec =>
      val workers = new PoolWork(work, dispatcher)
      failAfter(1.second)(workers.start())
      assert(!dispatcher.future.isCompleted)
      val (threads, values) = Await.result(dispatcher.future, 6.seconds).unzip // should take about 5 seconds
      assert(values.length === 5)
      assert(threads.toSet === tf.allThreads.toSet)
      assert(values === List(1, 2, 3, 4, 5) || values === List(1, 2, 3, 5, 4))
      assert(tf.allThreads.length === 4)
    }
  }

  test("64 threads, slow, semaphore", Slow) {
    val range = 1 to 64
    val ready = new CountDownLatch(64)
    val disp = new Dispatcher[Int, Int](range.iterator, Some(10))
    val pending = new AtomicInteger

    withLocalContext(newThreadPool(64)(TestThreadFactory)) { implicit exec =>
      val out = Future.traverse(range) { _ =>
        Future {
          ready.countDown()
          assert(ready.await(1, SECONDS), "not enough threads available")
          val i = disp.next().get
          assert(pending.incrementAndGet() <= 10) // bounded by semaphore
          Thread.sleep(1000)
          assert(!disp.future.isCompleted)
          pending.decrementAndGet()
          val r = disp.report(i)
          assert(!r || disp.future.isCompleted)
          r
        }
      }
      val f = for {
        reports <- out
        results <- disp.future
      } yield {
        assert(reports.count(identity) === 1) // 1 true, others false
        assert(results.length === 64)
        assert(results.toSet === range.toSet)
      }
      Await.ready(f, 8.seconds) // should take 7 seconds
    }
  }

  test("64 threads, fast, no semaphore") {
    val range = 1 to 64
    val ready = new CountDownLatch(64)
    val disp = new Dispatcher[Int, Int](range.iterator)

    withLocalContext(newThreadPool(64)(TestThreadFactory)) { implicit exec =>
      val out = Future.traverse(range) { _ =>
        Future {
          ready.countDown()
          assert(ready.await(1, SECONDS), "not enough threads available")
          disp.report(disp.next().get)
        }
      }
      for {
        reports <- out
        results <- disp.future
      } yield {
        assert(reports.count(identity) === 1) // 1 true, others false
        assert(results.length === 64)
        assert(results.toSet === range.toSet)
      }
    }
  }

  /* all in parallel; takes 3 seconds (longest task) */
  test("timing 1", Slow) {
    testTiming(List(1, 2, 3, 2.1, 1, 2), None, None, expected = 3)
  }

  /* all in parallel; takes 3 seconds (longest task) */
  test("timing 2", Slow) {
    testTiming(List(1, 2, 3, 2.1, 1, 2), Some(10), None, expected = 3)
  }

  /* 2 at a time:
   * - at time 0, a thread picks 5 and a thread picks 1
   * - at time 1, task 1 completes and a thread picks 2
   * - at time 3, task 2 completes and a thread picks 3
   * - at time 5, task 5 completes
   * - at time 6, task 3 completes
   */
  test("timing 3", Slow) {
    testTiming(List(5, 1, 2, 3), None, Some(2), expected = 6)
  }

  /* 2 at a time:
   * - at time 0, a thread picks 5 and a thread picks 1
   * - at time 1, task 1 completes and a thread picks 2
   * - at time 3, task 2 completes and a thread picks 3
   * - at time 5, task 5 completes
   * - at time 6, task 3 completes
   */
  test("timing 4", Slow) {
    testTiming(List(5, 1, 2, 3), Some(2), None, expected = 6)
  }

  // GrepApp tests

  lazy val alice = Using.resource(Source.fromResource("alice.txt"))(_.getLines().toList)
  lazy val usconst = Using.resource(Source.fromResource("usconst.txt"))(_.getLines().toList)

  def aliceText: TextSource = new TextSource {
    val name = "alice.txt"
    val lines = alice.iterator
  }

  def usconstText: TextSource = new TextSource {
    val name = "usconst.txt"
    val lines = usconst.iterator
  }

  test("1 file, no match") {
    val app = new GrepApp(0, 1.second)
    assert(app.find("(?i:education)".r, List(usconstText)).isEmpty)
  }

  test("2 files") {
    val app = new GrepApp(0, 1.second)
    val l = app.find("(?i:vote)".r, List(aliceText, usconstText))
    assert(l.length === 35)
    val al = l.filter(_.name == "alice.txt")
    assert(al.length === 1)
    assert(al.head.lineNumber === 1734)
    assert(l.map(_.lineNumber).toSet ===
      Set(56, 73, 147, 155, 286, 288, 289, 293, 296, 299, 300, 304, 305, 309, 600, 603, 604, 605,
        606, 611, 613, 616, 618, 619, 627, 655, 672, 687, 702, 729, 813, 828, 856, 863, 1734))
  }
}
