package grading

import org.scalatest.funsuite.AnyFunSuite

class SampleTests extends AnyFunSuite {
  import dummy.Easy

  test("sample") {
    assert(Easy.add(2, 2) === 4)
  }
}
