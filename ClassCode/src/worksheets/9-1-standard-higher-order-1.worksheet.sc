import scala.annotation.tailrec

// There are numerous higher-order methods implemented
// on collections in Scala, some of which tie directly
// into language features like for / yield. We'll talk
// about some of the important methods that take
// predicate functions as arguments first (predicate
// functions are those that return a Boolean value)
val nums = List(2, 3, 1, 33, 2, 5, 42, 101, 1, 3)

// find() returns the first item matching the passed
// condition in an Option (None if not found)
nums.find(_ > 5)

// exists returns true if there's an element matching
// the passed condition, while forall only returns
// true if they all do
nums.exists(_ > 10)
nums.forall(_ > 10)

// Let's look at how we might write the exists function
// note that "test" is the predicate function being
// passed in
@tailrec
def exists1[A](list: List[A], test: A => Boolean): Boolean = list match {
  case Nil => false
  case h :: t => if (test(h)) true else exists1(t, test)
}

// It could also be written without list matching. Note how
// the && and  || operation still allows for tail recursion
// and how easy it is to change from exists to forall
@tailrec
def exists[A](list: List[A], test: A => Boolean): Boolean =
  list.nonEmpty && (test(list.head) || exists(list.tail, test))

exists(nums, (x:Int) => x > 10) == exists1(nums, (x:Int) =>  x > 10)

// count returns how many elements pass the test
nums.count(_ > 10)

// We could write it similarly to the others,
// but it isn't tail recursive
def count1[A](list: List[A], test: A => Boolean): Int = list match {
  case Nil => 0
  case h :: t => if (test(h)) 1 + count1(t, test) else count1(t, test)
}

// We need a helper function to make it tail recursive
def count[A](list: List[A], test: A => Boolean): Int = {
  @tailrec
  def addCount(l: List[A], c: Int): Int = l match {
    case Nil => c
    case h :: t => addCount(t, if (test(h)) c + 1 else c)
  }
  addCount(list, 0)
}

count(nums, (_: Int) > 10)

// Here are a few methods that return modified
// collections. Note that partition gives a pair
// of filter/filterNot results
nums.filter(_ > 10)
nums.filterNot(_ > 10)
nums.partition(_ > 10)

// How might we write our own filter method?
def filter[A](list: List[A], test: A => Boolean): List[A] = list match {
  case Nil => Nil
  case h :: t => if (test(h)) h :: filter(t, test) else filter(t, test)
}

filter(nums, (_: Int) > 10)

// The methods takeWhile and dropWhile work
// similarly, but stop once the condition fails
nums.takeWhile(_ < 10)
nums.filter(_ < 10)

nums.dropWhile(_ < 10)

nums.span(_ < 10)

// How might we write takeWhile?
def takeWhile[A](list: List[A], test: A => Boolean): List[A] = list match {
  case Nil => Nil
  case h :: t => if (test(h)) h :: takeWhile(t, test) else Nil
}

takeWhile(nums, (_: Int) < 10)

// How might we transform this to make it
// tail recursive?
def takeWhile_tail[A](list: List[A], test: A => Boolean): List[A] = {
  @tailrec
  def helper(l: List[A], acc: List[A]): List[A] = l match {
    case Nil => acc
    case h :: t => if (test(h)) helper(t, h :: acc) else acc
  }
  helper(list, Nil).reverse
}

takeWhile_tail(nums, (_: Int) < 10)
