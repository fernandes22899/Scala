package grading

import edu.unh.cs.mc.grading.Grading

class EasySuite extends SampleTests with Grading {
  import dummy.Easy

  test("grading") {
    assert(Easy.add(42, 0) === 42)
  }
}
