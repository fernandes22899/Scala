import scala.annotation.tailrec

'U' + 'N' - 'H' - 'C' - 'S'
'F' + 'u' - 't' - 'u' - 'r' + 'e'

def chars(s: String) = (for (c <- s) yield c.toInt).toList

val l1 = chars("UNHCS")
val l2 = chars("Future")

def targets(nums: List[Int]): Set[Int] = nums match {
  case Nil => Set(0)
  case n :: others =>
    val fromTail = targets(others)
    val plus = for (x <- fromTail) yield x + n
    val minus = for (x <- fromTail) yield x - n
    plus union minus
}

val t1 = targets(l1)
val t2 = targets(l2)
t1 intersect t2

def cleanTargets(nums: List[Int]): Set[Int] =
  for (t <- targets(nums.tail)) yield t + nums.head

val r1 = cleanTargets(l1)
val r2 = cleanTargets(l2)
r1 intersect r2

def reach(nums: List[Int], target: Int): Option[List[Int]] = nums match {
  case Nil => if (target == 0) Some(Nil) else None
  case n :: others =>
    reach(others, target - n) match {
      case Some(sol1) => Some(n :: sol1)
      case None => reach(others, target + n) match {
        case Some(sol2) =>Some(-n :: sol2)
        case None => None
      }
    }
}

reach(l1, -59)
reach(l1, -58)
reach(l1, 59)

def cleanReach(nums: List[Int], target: Int): Option[List[Int]] =
  reach(nums.tail, target - nums.head) match {
    case Some(sol) => Some(nums.head :: sol)
    case None => None
  }

val s1 = cleanReach(l1, -59)
cleanReach(l1, 59)

def mkString(nums: List[Int]): String = {
  @tailrec
  def add(l: List[Int], buffer: StringBuilder): String = l match {
    case Nil => buffer.result()
    case n :: others =>
    if (n > 0) buffer.append(" + ").append(n.toChar)
    else buffer.append(" - ").append((-n).toChar)
    add(others, buffer)
  }
  add(nums.tail, new StringBuilder(nums.head.toChar.toString))
}

mkString(s1.get)

def reachAll(nums: List[Int], target: Int): Set[List[Int]] = nums match {
  case Nil => if (target == 0) Set(Nil) else Set.empty
  case n :: others =>
    val plus = for (list <- reachAll(others, target - n)) yield n :: list
    val minus = for (list <- reachAll(others, target + n)) yield -n :: list
    plus union minus
}

reachAll(l1, -59)
reachAll(l2, -59)

def cleanReachAll(nums: List[Int], target: Int): Set[List[Int]] =
  for (list <- reachAll(nums.tail, target - nums.head)) yield nums.head :: list

def printAll(s1: String, s2: String): Unit = {
  val nums1 = chars(s1)
  val nums2 = chars(s2)
  val common: Set[Int] = cleanTargets(nums1).intersect(cleanTargets(nums2))
  for (target <- common) {
    for (l <- cleanReachAll(nums1, target))
      println(s"${mkString(l)} -> $target")
    for (l <- cleanReachAll(nums2, target)) {
      println(s"${mkString(l)} -> $target")
    }
    println("----")
  }
}

printAll("UNHCS", "Future")
printAll("ChooseCS", "beHappy")
printAll("recursion", "tooeasy")
