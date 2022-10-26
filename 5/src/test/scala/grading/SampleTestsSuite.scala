package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.time.SpanSugar._

class SampleTestsSuite extends SampleTests with GradingRun {
  override val longTimeLimit = 10.seconds
}
