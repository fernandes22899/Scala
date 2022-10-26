package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow
import org.scalatest.time.SpanSugar._

class TimingTestSuite extends AnyFunSuite with TimingTests with GradingRun {

  override val longTimeLimit = 15.seconds

  for {
    (times, threads, bound, expected) <- List(
      (List[Double](1, 2, 3, 2.1, 1, 2), None, Some(3), 5),
      (List[Double](1, 2, 3, 2.1, 1, 2), Some(3), None, 5),
      ((10 to 100).toList.map(_ / 10.0), None, Some(91), 10),
      ((10 to 100).toList.map(_ / 10.0), Some(91), None, 10),
      (List[Double](5, 1, 2, 3), None, Some(3), 5),
      (List[Double](5, 1, 2, 3), Some(3), None, 5),
      (List[Double](5, 1, 2, 3), Some(4), Some(2), 6),
      (List[Double](5, 1, 2, 3), Some(2), Some(4), 6),
      (List[Double](1, 5, 1, 5, 4, 1, 1), None, Some(3), 6),
      (List[Double](1, 5, 1, 5, 4, 1, 1), Some(3), None, 6),
      (List[Double](1, 5, 1, 5, 4, 1, 1), Some(7), Some(3), 6),
      (List[Double](1, 5, 1, 5, 4, 1, 1), Some(3), Some(7), 6),
      (List[Double](1, 1, 1, 5, 1, 1, 1, 1), None, Some(2), 6),
      (List[Double](1, 1, 1, 5, 1, 1, 1, 1), Some(2), None, 6),
      (List[Double](1, 1, 1, 5, 1, 1, 1, 1), Some(8), Some(2), 6),
      (List[Double](1, 1, 1, 5, 1, 1, 1, 1), Some(2), Some(8), 6),
    )
  } {
    val workType = threads.map(n => s" with $n worker threads").getOrElse(" with a thread pool")
    val end = bound.map(n => s" with $n permits").getOrElse("")
    val timeList = {
      val str = times.mkString("[", ", ", "]")
      if (str.lengthIs > 50) str.take(47) + "..." else str
    }
    test(s"$timeList$workType$end", Slow) {
      testTiming(times, threads, bound, expected)
    }
  }
}
