package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.time.SpanSugar._

class SampleChangerSuite extends SampleChangerTests with GradingRun {
  override val longTimeLimit = 5.seconds
}
