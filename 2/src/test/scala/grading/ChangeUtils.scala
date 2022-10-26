package grading

import org.scalatest.Assertions

import scala.util.Using

trait ChangeUtils extends Assertions {

  import cs671.change.Changer

  import scala.io.Source

  trait ChangeProblem {
    def target: Int

    def numbers: List[Int]

    override def toString = {
      val nums = numbers mkString ", "
      s"change $target with $nums"
    }

    protected def checkSolution(sol: List[Int], error: Int): Unit = {
      val sum = sol.sum + error
      assert(sum === target, s"; solution (plus error) doesn't add up to target for $this ")
      assert(sol.diff(numbers).isEmpty, s"; solution uses extra numbers for $this ")
    }

    def checkChange(): Unit

    def checkChangeBest(): Unit

    def checkChangeApprox(): Unit
  }

  /* Lines in a file are as follows:
   * x y z n1 n2 n3 ...
   * where x is 1 for feasible problems (and y is the minimum length) and x is 0 for infeasible
   * problems (and y is the minimum gap). z is the target and n1, n2, ... are the numbers
   */
  def readProblems(source: Source): List[ChangeProblem] = {
    val whitespace = """\s+""".r

    def parseLine(line: String): ChangeProblem =
      try {
        (whitespace.split(line).toList.map(_.toInt): @unchecked) match {
          case 1 :: len :: target :: nums => Feasible(target, nums, len)
          case 0 :: gap :: target :: nums => Infeasible(target, nums, gap)
        }
      }
      catch {
        case _: NumberFormatException | _: MatchError =>
          throw new IllegalArgumentException(s"cannot parse line: '$line")
      }

    Using.resource(source) { in =>
      in.getLines().map(_.trim).filter(_.nonEmpty).map(parseLine).toList
    }
  }

  // Problems with a solution (of known minimal length)
  case class Feasible(target: Int, numbers: List[Int], minLength: Int) extends ChangeProblem {
    def checkChange() = {
      val sol = Changer.change(numbers, target)
      assert(sol.nonEmpty, s"; exact solution not found for $this ")
      checkSolution(sol.get, 0)
    }

    def checkChangeBest() = {
      val sol = Changer.changeBest(numbers, target)
      assert(sol.nonEmpty, s"; exact solution not found for $this ")
      assert(sol.get.length === minLength, s"; shortest solution has wrong length for $this ")
      checkSolution(sol.get, 0)
    }

    def checkChangeApprox() = {
      val (error, sol) = Changer.changeApprox(numbers, target)
      assert(error === 0, s"; exact solution not found for $this ")
      checkSolution(sol, error)
    }
  }

  // Problems without a solution (of known minimal gap)
  case class Infeasible(target: Int, numbers: List[Int], minDiff: Int) extends ChangeProblem {
    def checkChange() = {
      val sol = Changer.change(numbers, target)
      assert(sol.isEmpty, s"; solution found where none exists for $this ")
    }

    def checkChangeBest() = {
      val sol = Changer.changeBest(numbers, target)
      assert(sol.isEmpty, s"; solution found where none exists for $this ")
    }

    def checkChangeApprox() = {
      val (error, sol) = Changer.changeApprox(numbers, target)
      assert(error === minDiff, s"; approximate solution has incorrect gap for $this ")
      checkSolution(sol, error)
    }
  }
}
