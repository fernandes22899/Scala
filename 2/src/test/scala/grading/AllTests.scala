package grading

import org.scalatest.Suites

class AllTests extends Suites(
  new PolynomialSuite,
  new ChangerSuite
)
