package grading

class AllTests extends org.scalatest.Suites(
  new SampleTestsSuite,
  new DispatcherSuite,
  new TimingTestSuite,
  new GrepAppSuite
)
