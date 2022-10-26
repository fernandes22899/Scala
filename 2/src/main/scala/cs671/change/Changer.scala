package cs671.change

/**
  * Implements a ''change'' solver.
  * The problem to solve is to pick numbers from a multiset that add up to a given target.
  * This is similar to giving change by taking bills and coins from a cash register (hence the name).
  * (In theoretical computer science, this problem is known as
  * [[https://en.wikipedia.org/wiki/Subset_sum_problem subset-sum]].
  *
  * This object solves three variants of the problem:
  *  - `change` finds an exact solution, if there is one;
  *  - `changeBest` finds an exact solution of minimum length, if there is at least one solution;
  *  - `changeApprox` finds numbers that add up to the target or to the largest possible value
  *   less than the target.
  *
  * The algorithms used in this implementation are not efficient (exponential in time).
  *
  * All methods throw `IllegalArgumentException` if the target is negative or if the given sequence
  * contains at least one non-positive integer.
  */

object Changer {

  /**
    * Finds elements from the given multiset that add up to the target, if possible.
    * The solution is not guaranteed to be of minimal length.
    */
  @throws[IllegalArgumentException]("if target is negative or any list number is not positive")
  def change(numbers: List[Int], target: Int): Option[List[Int]] =  numbers match {
    case _ if(!numbers.forall(num => num > 0)) => throw new IllegalArgumentException("numbers are negative")
    case _ if(!numbers.forall(num => num > 0) && (target < 0)) => throw new IllegalArgumentException("numbers are negative")
    case Nil => {if(target == 0) Some(List()) else None} //if list is empty and target is 0, perfect. Otherwise,None
    case (h1 :: t1) => {//testing head and tail
      if (target == 0) {return Some(List())}//returns empty list
      change(t1, target - h1) match {//checking for creation of successful branch
        case None => change(t1, target) //If target - h1 unavailable then call just target
        case elements => Some(h1 :: elements.get)//if available, then add head to branch then tail
      }
    }
  }

  /**
    * Finds elements from the given multiset that add up to the target, if possible.
    * The solution is guaranteed to be of minimal length, which makes this method slightly
    * slower than [[change]].
    */
  @throws[IllegalArgumentException]("if target is negative or any list number is not positive")
  def changeBest(numbers: List[Int], target: Int): Option[List[Int]] = numbers match {
    case _ if(!numbers.forall(num => num > 0)) => throw new IllegalArgumentException("numbers are negative")
    case _ if(!numbers.forall((num => num > 0)) && (target < 0)) => throw new IllegalArgumentException("numbers are negative")
    case Nil => {if(target == 0) Some(List()) else None}//if list is empty and target is 0, perfect. Otherwise,None
    case (h1 :: t1) => { //testing head and tail
      if(target == 0) {return Some(List())}//returns empty to successful branch
      (changeBest(t1, target - h1), changeBest(t1, target)) match { //trying to find both branches
        case (None, None) => None //failed to explore both branches
        case (ele, None) => Some(h1 :: ele.get) //getting list if no elements
        case (None, divEle) => divEle //returns just elements in list
        case (ele, divEle) => {//get shorter solution
          if(ele.get.length + 1 < divEle.get.length) Some(h1 :: ele.get) else divEle
        }
      }
    }
  }

  /**
    * Finds elements from the given multiset that add up to the target, if possible.
    * If no exact solution exists, this method builds a list that adds up to the largest
    * possible value less than the given target.
    * There are no guarantees regarding the length of the solution.
    *
    * @return a pair `(error, list)` where `error` is nonnegative, guaranteed to be minimal
    *         and `error + list.sum == target`.  Note that `error` is 0 if an exact solution exists.
    */
  @throws[IllegalArgumentException]("if target is negative of any list number is not positive")
  def changeApprox(numbers: List[Int], target: Int): (Int, List[Int]) = numbers match {
    case _ if(!numbers.forall(num => num > 0)) => throw new IllegalArgumentException("numbers are negative")
    case _ if(!numbers.forall((num => num > 0)) && (target < 0)) => throw new IllegalArgumentException("numbers are negative")
    //case _ if(target < 0) => throw new IllegalArgumentException("target is negative")
    case Nil => {if(target == 0) (0, List()) else (target, List())} //if list is empty and target is 0, perfect. Otherwise,compare the element and list
    case h1 :: t1 => { //testing head and tail
      if(target == 0) return (0, List())//returns empty to successful branch
      (changeApprox(t1, target - h1), changeApprox(t1, target)) match {//check branches
        case(ele, divEle) => (ele._1 == 0, divEle._1 == 0) match {
          case (true, _) => (0, h1 :: ele._2) //successful list
          case ( _, true ) => divEle //returns element
          case (false,false) => {//no solution
            if((ele._1 < divEle._1) && (ele._1 > 0)) (ele._1, h1 :: ele._2) else divEle
          }
        }
      }
    }
  }

  /** A simple command-line application that tries all 3 methods of the object on the same problem. */
  def main(args: Array[String]): Unit = {
    def timePrint(name: String, code: => Any) = {
      val start = System.nanoTime()
      val res = code
      val end = System.nanoTime()
      println(f"$name: $res (${(end - start) / 1e6}%.1f millis)")
    }

    try {
      require(args.length > 1)
      val intArgs = args.map(_.toInt)
      val numbers = intArgs.init.toList
      val target = intArgs.last
      timePrint("change", change(numbers, target))
      timePrint("changeBest", changeBest(numbers, target))
      timePrint("changeApprox", changeApprox(numbers, target))
    }
    catch {
      case _: IllegalArgumentException | _: NumberFormatException =>
        println("Usage: Changer nums... target")
    }
  }
}
