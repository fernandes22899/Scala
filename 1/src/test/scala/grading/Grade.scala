package grading

import edu.unh.cs.mc.grading.GraderApp

object Grade extends GraderApp(
  20 -> new RefSuite,
  20 -> new PracticeSuite,
  60 -> new MastermindSuite
)
