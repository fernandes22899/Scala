package grading

object Grade extends edu.unh.cs.mc.grading.GraderApp(
  20 -> new SampleTestsSuite,
  30 -> new DispatcherSuite,
  30 -> new TimingTestSuite,
  20 -> new GrepAppSuite
)
