import scala.annotation.tailrec

// Functional languages support the use of higher-order
// functions--functions that take other functions as
// parameters or return them as return values. These
// functions do not need to have names.
// As an example, consider doing a linear search
// through a list.
val grades = List(88, 91, 78, 69, 100, 98, 70)

@tailrec
def findEqual[A](list: List[A], target: A): Option[A] = list match {
  case Nil => None
  case h :: t => if (h == target) Some(h) else findEqual(t, target)
}

findEqual(grades, 98)
findEqual(grades, 97)

// The above is great if you want to find a value
// by using a copy of the value you're looking for,
// but what if you want to find something that is
// greater, lesser, or has some component that you're
// interested in finding?
// You could write another function...
@tailrec
def findLargerThan90(list: List[Int]): Option[Int] = list match {
  case Nil => None
  case h :: t => if (h > 90) Some(h) else findLargerThan90(t)
}

findLargerThan90(grades)

// ...for every single little thing, even though
// it's otherwise all the same code...
@tailrec
def findLargerThan(list: List[Int], bound: Int): Option[Int] = list match {
  case Nil => None
  case h :: t => if (h > bound) Some(h) else findLargerThan(t, bound)
}

findLargerThan(grades, 80)

// ...or we could pass the Boolean test as a parameter.
// The type of a function uses => to indicate that it
// takes a parameter (or parameter list) as input
// (on the left of the =>) and returns some type (on
// the right of the =>). Now we can write this function
// more generally.
@tailrec
def find[A](list: List[A], test: A => Boolean): Option[A] = list match {
  case Nil => None
  case h :: t => if (test(h)) Some(h) else find(t, test)
}

// And now we can pass a function to it
def lessThan80(g: Int): Boolean = g < 80

find(grades, lessThan80)

// And we could create more sophisticated functions
// to help us out... But this is only the beginning!
def largerThan(bound: Int): Int => Boolean = {
  def largerThanBound(x: Int): Boolean = x > bound
  largerThanBound
}

find(grades, largerThan(70))
grades.find(largerThan(70))

