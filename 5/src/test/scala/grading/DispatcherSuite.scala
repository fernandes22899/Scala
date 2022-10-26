package grading

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.atomic.AtomicInteger

import edu.unh.cs.mc.grading.GradingRun
import edu.unh.cs.mc.utils.threads.Executors.{ newThreadPool, withLocalContext }
import edu.unh.cs.mc.utils.threads.TestThreadFactory
import org.scalactic.TimesOnInt._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow
import org.scalatest.time.SpanSugar._

import scala.concurrent._
import scala.concurrent.duration.Duration

class DispatcherSuite extends AnyFunSuite with GradingRun{

  import parallel.Dispatcher

  override val shortTimeLimit = 3.seconds
  override val longTimeLimit = 15.seconds

  def testSingle(n: Int, bound: Option[Int] = None): Unit = {
    val range = 1 to n
    val disp = new Dispatcher[Int, Int](range.iterator, bound)
    for (i <- range) {
      val w = disp.next()
      assert(w.contains(i))
      assert(!disp.future.isCompleted)
      assert(disp.report(i) === (i == n))
    }
    100 times {
      assert(disp.next().isEmpty)
      assert(disp.future.isCompleted)
      assert(disp.future.value.get.get === range)
    }
  }

  for {
    (tasks, bound) <- List(
      (1000, None),
      (1000, Some(1)),
      (1000, Some(10)),
      (1000, Some(2000)),
      (100_000, None),
      (100_000, Some(1)),
      (100_000, Some(10)),
    )
  } {
    val end = bound.map(b => s" and $b permits").getOrElse("")
    test(s"$tasks tasks with 1 thread$end") {
      testSingle(tasks, bound)
    }
  }

  def testMultiSlow(tasks: Int, threads: Int, bound: Option[Int] = None)(d: Duration) = {
    val range = 1 to tasks
    val ready = new CountDownLatch(tasks min threads)
    val disp = new Dispatcher[Int, Int](range.iterator, bound)
    val pending = new AtomicInteger

    withLocalContext(newThreadPool(threads)(TestThreadFactory)) { implicit exec =>
      val out = Future.traverse(range) { _ =>
        Future {
          ready.countDown()
          assert(ready.await(1, SECONDS), "not enough threads available")
          val i = disp.next().get
          for (b <- bound)
            assert(pending.incrementAndGet() <= b)
          SECONDS.sleep(1)
          assert(!disp.future.isCompleted)
          for (_ <- bound)
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
        assert(reports.count(identity) === 1)
        assert(results.length === tasks)
        assert(results.toSet === range.toSet)
      }
      Await.ready(f, d)
    }
  }

  // each task is 1 second
  def expectedTime(tasks: Int, threads: Int, bound: Option[Int] = None) = {
    val p = threads min bound.getOrElse(Int.MaxValue)
    val time = tasks / p + (tasks % p).sign
    (time + 1).seconds // add 1 second for overhead
  }

  for {
    (tasks, threads, bound) <- List(
      (10, 10, None),
      (10, 32, None),
      (64, 64, None),
      (64, 32, None),
      (64, 16, None),
      (1024, 128, None),
      (1024, 100, None),
      (131, 11, None),
      (25, 32, Some(10)),
      (25, 5, Some(10)),
      (64, 64, Some(64)),
      (64, 64, Some(32)),
      (64, 64, Some(10)),
      (1000, 128, Some(100)),
    )
  } {
    val end = bound.map(b => s" and $b permits").getOrElse("")
    test(s"$tasks slow tasks with $threads threads$end", Slow) {
      testMultiSlow(tasks, threads, bound)(expectedTime(tasks, threads, bound))
    }
  }

  def testMultiFast(tasks: Int, threads: Int, bound: Option[Int] = None) = {
    val range = 1 to tasks
    val ready = new CountDownLatch(tasks min threads)
    val disp = new Dispatcher[Int, Int](range.iterator, bound)

    withLocalContext(newThreadPool(threads)(TestThreadFactory)) { implicit exec =>
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
        assert(reports.count(identity) === 1)
        assert(results.length === tasks)
        assert(results.toSet === range.toSet)
      }
    }
  }

  for {
    (tasks, threads, bound) <- List(
      (10, 64, None),
      (64, 64, None),
      (1024, 64, None),
      (1024, 32, None),
      (1024, 16, None),
      (100000, 16, None),
      (10, 64, Some(5)),
      (64, 64, Some(30)),
      (1024, 64, Some(30)),
      (1024, 64, Some(2)),
      (100000, 64, Some(50)),
    )
  } {
    val end = bound.map(b => s" and $b permits").getOrElse("")
    test(s"$tasks fast tasks with $threads threads$end") {
      testMultiFast(tasks, threads, bound)
    }
  }

  def testManyThreads(tasks: Int, extra: Int, bound: Option[Int] = None): Unit = {
    val threads = tasks + extra
    val range = 1 to tasks
    val ready = new CountDownLatch(threads)
    val disp = new Dispatcher[Int, Int](range.iterator, bound)

    withLocalContext(newThreadPool(threads)(TestThreadFactory)) { implicit exec =>
      val out = Future.traverse(1 to threads) { _ =>
        Future {
          ready.countDown()
          assert(ready.await(1, SECONDS), "not enough threads available")
          for (i <- disp.next()) yield disp.report(i)
        }
      }
      for {
        reports <- out
        results <- disp.future
      } yield {
        assert(reports.count(_.isEmpty) === extra)
        assert(reports.count(_.contains(true)) === 1)
        assert(results.length === tasks)
        assert(results.toSet === range.toSet)
      }
    }
  }

  for {
    (tasks, extra, bound) <- List(
      (32, 32, None),
      (1, 63, None),
      (64, 64, None),
      (1, 127, None),
      (10, 90, None),
      (99, 1, None),
      (1, 99, None),
      (32, 32, Some(50)),
      (32, 32, Some(100)),
      (1, 63, Some(50)),
      (1, 63, Some(100)),
      (99, 1, Some(50)),
      (99, 1, Some(99)),
      (99, 1, Some(100)),
      (99, 1, Some(200)),
    )
  } {
    val end = bound.map(b => s" and $b permits").getOrElse("")
    test(s"$tasks tasks with $extra extra threads$end") {
      testManyThreads(tasks, extra, bound)
    }
  }

  test("exception 1") {
    val disp = new Dispatcher[Int, String](Iterator(1))
    assert(disp.next().nonEmpty)
    assert(disp.next().isEmpty)
    assert(disp.next().isEmpty)
    assert(disp.report("foo"))
    assertThrows[IllegalStateException](disp.report("bar"))
  }

  test("exception 2") {
    val disp = new Dispatcher[Int, String](Iterator(1, 2))
    assert(disp.next().nonEmpty)
    assert(!disp.report("foo"))
    assertThrows[IllegalStateException](disp.report("bar"))
  }

  test("empty 1") {
    val disp = new Dispatcher[Nothing, Any](Iterator.empty)
    assertThrows[IllegalStateException](disp.report(new Object))
  }

  test("empty 2") {
    val disp = new Dispatcher[Nothing, Any](Iterator.empty)
    assert(disp.future.isCompleted)
    assert(disp.next().isEmpty)
    assertThrows[IllegalStateException](disp.report(new Object))
  }

  test("empty next 1") {
    val disp = new Dispatcher[Int, String](Iterator(1, 2), Some(3))
    assert(disp.next().contains(1))
    assert(disp.next().contains(2))
    100 times assert(disp.next().isEmpty)
  }

  test("empty next 2") {
    val disp = new Dispatcher[Int, String](Iterator(1, 2), Some(1))
    assert(disp.next().contains(1))
    assert(!disp.report("foo"))
    assert(disp.next().contains(2))
    assert(disp.report("bar"))
    100 times assert(disp.next().isEmpty)
  }
}
