package grading

import org.scalatest.Suites

class AllTests extends Suites(
  new RefSuite,
  new PracticeSuite,
  new MastermindSuite
)
