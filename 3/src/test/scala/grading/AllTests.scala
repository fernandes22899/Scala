package grading

class AllTests extends org.scalatest.Suites(
  new SampleSpaceSuite,
  new SpaceSuite,
  new SampleQuadTreeFigureSuite,
  new QuadTreeFigureSuite,
  new SampleQuadTreeSuite,
  new QuadTreeSuite,
  new CitySuite
)
