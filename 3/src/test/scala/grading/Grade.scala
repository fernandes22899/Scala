package grading

import edu.unh.cs.mc.grading.GraderApp

object Grade extends GraderApp(
  5 -> new SampleSpaceSuite,
  5 -> new SpaceSuite,
  15 -> new SampleQuadTreeFigureSuite,
  20 -> new QuadTreeFigureSuite,
  20 -> new SampleQuadTreeSuite,
  25 -> new QuadTreeSuite,
  10 -> new CitySuite
)
