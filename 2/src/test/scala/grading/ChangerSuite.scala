package grading

import edu.unh.cs.mc.grading.{ Controls, GradingRun }
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow

class ChangerSuite extends AnyFunSuite with ChangeUtils with Controls with GradingRun {

  import cs671.change.Changer.{ change, changeApprox, changeBest }

  import scala.io.Source

  val smallProblems = readProblems(Source.fromResource("10000-small-change-problems.txt"))

  val largeProblems = readProblems(Source.fromResource("10000-large-change-problems.txt"))

  // Series of randomly generated problems are read from files
  test("'change' on first 1000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.take(1000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on next 4000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.slice(1000, 5000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on all remaining small problems [2pts]", Slow) {
    for (pb <- smallProblems.drop(5000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on first 100 large problems [2pts]", Slow) {
    for (pb <- largeProblems.take(100)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on next 900 large problems [2pts]", Slow) {
    for (pb <- largeProblems.slice(100, 1000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on next 4000 large problems [2pts]", Slow) {
    for (pb <- largeProblems.slice(1000, 5000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'change' on all remaining large problems [2pts]", Slow) {
    for (pb <- largeProblems.drop(5000)) doInterruptibly {
      pb.checkChange()
    }
  }

  test("'changeBest' on first 1000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.take(1000)) doInterruptibly {
      pb.checkChangeBest()
    }
  }

  test("'changeBest' on next 4000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.slice(1000, 5000)) doInterruptibly {
      pb.checkChangeBest()
    }
  }

  test("'changeBest' on all remaining small problems [2pts]", Slow) {
    for (pb <- smallProblems.drop(5000)) doInterruptibly {
      pb.checkChangeBest()
    }
  }

  test("'changeBest' on first 100 large problems [2pts]", Slow) {
    for (pb <- largeProblems.take(100)) doInterruptibly {
      pb.checkChangeBest()
    }
  }

  test("'changeBest' on next 400 large problems [2pts]", Slow) {
    for (pb <- largeProblems.slice(100, 500)) doInterruptibly {
      pb.checkChangeBest()
    }
  }

  test("'changeApprox' on first 1000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.take(1000)) doInterruptibly {
      pb.checkChangeApprox()
    }
  }

  test("'changeApprox' on next 4000 small problems [2pts]", Slow) {
    for (pb <- smallProblems.slice(1000, 5000)) doInterruptibly {
      pb.checkChangeApprox()
    }
  }

  test("'changeApprox' on all remaining small problems [2pts]", Slow) {
    for (pb <- smallProblems.drop(5000)) doInterruptibly {
      pb.checkChangeApprox()
    }
  }

  test("'changeApprox' on first 100 large problems [2pts]", Slow) {
    for (pb <- largeProblems.take(100)) doInterruptibly {
      pb.checkChangeApprox()
    }
  }

  test("'changeApprox' on next 900 large problems [2pts]", Slow) {
    for (pb <- largeProblems.slice(100, 1000)) doInterruptibly {
      pb.checkChangeApprox()
    }
  }

  test("'changeApprox' on a large difference problem [2pts]") {
    val pb = Infeasible(
      3900,
      List(
        168, 243, 274, 254, 7, 211, 256, 257, 169, 9, 298, 191, 175, 271, 227, 10, 260, 266, 216,
        197
      ),
      109
    )
    pb.checkChangeApprox()
  }

  // parameter checking

  test("Empty sequences are valid") {
    change(Nil, 42)
    changeBest(Nil, 42)
    changeApprox(Nil, 42)
  }

  test("Zeros in lists are invalid") {
    assertThrows[IllegalArgumentException] {
      change(List(1, 0), 42)
    }
    assertThrows[IllegalArgumentException] {
      changeBest(List(1, 0), 42)
    }
    assertThrows[IllegalArgumentException] {
      changeApprox(List(1, 0), 42)
    }
  }

  test("Negative numbers in lists are invalid") {
    assertThrows[IllegalArgumentException] {
      change(List(1, -2), 42)
    }
    assertThrows[IllegalArgumentException] {
      changeBest(List(1, -2), 42)
    }
    assertThrows[IllegalArgumentException] {
      changeApprox(List(1, -2), 42)
    }
  }

  test("Negative targets are invalid") {
    assertThrows[IllegalArgumentException] {
      change(List(1, 2, 3), -42)
    }
    assertThrows[IllegalArgumentException] {
      changeBest(List(1, 2, 3), -42)
    }
    assertThrows[IllegalArgumentException] {
      changeApprox(List(1, 2, 3), -42)
    }
  }
}
