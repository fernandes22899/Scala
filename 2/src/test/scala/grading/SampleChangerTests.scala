package grading

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow

class SampleChangerTests extends AnyFunSuite with ChangeUtils {

  import cs671.change.Changer.{ change, changeApprox, changeBest }

  // Samples from assignment text

  test("5 with [1 2 3 5]") {
    change(List(1, 2, 3, 5), 5) match {
      case Some(l) if l.length == 2 => assert(l.sorted === List(2, 3))
      case Some(l) if l.length == 1 => assert(l === List(5))
      case _                        => fail("solution not found")
    }
  }

  test("221 with [50 10 1 69 51 9 73 89 30]") {
    change(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 221) match {
      case Some(l) if l.length == 4 => assert(l.sorted === List(9, 50, 73, 89))
      case Some(l) if l.length == 5 => assert(l.sorted === List(1, 30, 50, 51, 89))
      case _                        => fail("solution not found")
    }
  }

  test("225 with [50 10 1 69 51 9 73 89 30]") {
    assert(change(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 225).isEmpty)
  }

  test("5 with [1 2 3 5], best") {
    assert(changeBest(List(1, 2, 3, 5), 5).contains(List(5)))
  }

  test("221 with [50 10 1 69 51 9 73 89 30], best") {
    assert(
      changeBest(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 221)
        .map(_.sorted)
        .contains(List(9, 50, 73, 89))
    )
  }

  test("225 with [50 10 1 69 51 9 73 89 30], best") {
    assert(changeBest(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 225).isEmpty)
  }

  test("5 with [1 2 3 5], approx") {
    val (d, l) = changeApprox(List(1, 2, 3, 5), 5)
    assert(d === 0)
    assert(l.sum === 5)
  }

  test("221 with [50 10 1 69 51 9 73 89 30], approx") {
    val (d, l) = changeApprox(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 221)
    assert(d === 0)
    assert(l.sum === 221)
  }

  test("225 with [50 10 1 69 51 9 73 89 30], approx") {
    val (d, l) = changeApprox(List(50, 10, 1, 69, 51, 9, 73, 89, 30), 225)
    assert(d === 1)
    assert(l.sum === 224)
  }

  test("12 with [3 5 5 8 10]") {
    assert(change(List(3, 5, 5, 8, 10), 12).isEmpty)
  }

  test("12 with [3 5 5 8 10], best") {
    assert(changeBest(List(3, 5, 5, 8, 10), 12).isEmpty)
  }

  test("12 with [3 5 5 8 10], approx") {
    val (d, l) = changeApprox(List(3, 5, 5, 8, 10), 12)
    assert(d == 1)
    assert(l.sorted === List(3, 8))
  }

  // More sample problems

  val trivial         = Feasible(0, List.empty, 0)
  val smallFeasible   = Feasible(8, List(1, 3, 7), 2)
  val smallInfeasible = Infeasible(6, List(1, 3, 7), 2)
  val mediumFeasible = Feasible(
    417,
    List(16, 87, 33, 63, 41, 89, 74, 58, 65, 18, 45, 26),
    7
  )
  val mediumInfeasible = Infeasible(
    627,
    List(25, 25, 43, 4, 50, 50, 18, 21, 80, 24, 62, 73, 54, 5, 62, 91),
    1
  )
  val largeFeasible = Feasible(
    712,
    List(42, 82, 86, 78, 6, 27, 98, 49, 16, 85, 10, 2, 35, 82, 50, 60, 45, 20, 18, 60),
    10
  )
  val largeInfeasible = Infeasible(
    812,
    List(88, 69, 62, 22, 58, 55, 24, 23, 24, 17, 3, 21, 46, 54, 42, 79, 29, 32, 83, 14),
    1
  )

  val largerFeasible = Feasible(
    997,
    List(56, 48, 30, 2, 42, 93, 15, 11, 9, 90, 18, 31, 88, 38, 98, 60, 55, 69, 25, 88, 69, 82, 49,
      91, 68),
    14
  )

  val largerInfeasible = Infeasible(
    966,
    List(57, 22, 39, 83, 37, 44, 33, 48, 66, 89, 34, 5, 13, 7, 28, 33, 58, 3, 44, 18, 49, 82, 25,
      48, 20),
    1
  )

  val largeDiff = Infeasible(
    11663,
    List(667, 555, 573, 754, 641, 996, 452, 467, 243, 136, 899, 597, 118, 766, 852, 749, 833, 577,
      140, 683),
    83
  )

  // The same 'change' problems are run with change, changeBest and changeApprox

  val toTest = Map[String, ChangeProblem => Unit](
    "change"       -> (_.checkChange()),
    "changeBest"   -> (_.checkChangeBest()),
    "changeApprox" -> (_.checkChangeApprox())
  )

  for ((name, changeFn) <- toTest) {

    test(s"'$name' on a trivial problem") {
      changeFn(trivial)
    }

    test(s"'$name' on a small feasible problem") {
      changeFn(smallFeasible)
    }

    test(s"'$name' on a small infeasible problem") {
      changeFn(smallInfeasible)
    }

    test(s"'$name' on a medium feasible problem") {
      changeFn(mediumFeasible)
    }

    test(s"'$name' on a medium infeasible problem") {
      changeFn(mediumInfeasible)
    }

    test(s"'$name' on a large feasible problem") {
      changeFn(largeFeasible)
    }

    test(s"'$name' on a large infeasible problem") {
      changeFn(largeInfeasible)
    }

    test(s"'$name' on a larger feasible problem", Slow) {
      changeFn(largerFeasible)
    }

    test(s"'$name' on a larger infeasible problem", Slow) {
      changeFn(largerInfeasible)
    }

    test(s"'$name' on a large difference (infeasible) problem", Slow) {
      changeFn(largerInfeasible)
    }
  }
}
