package grading

import edu.unh.cs.mc.grading.GraderApp

object Grade extends GraderApp(
  60 -> new PolynomialSuite,
  40 -> new ChangerSuite
)
