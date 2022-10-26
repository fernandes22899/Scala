// First major topic of functional programming: Recursion
// Recursion involes writing a funtion that works by
// calling itself on a smaller sub-problem, and then building
// an answer on top of the result. The point is not to use
// any mutable variables, but only to compose functions calls.
// To build a recursion, you...
//   1) Decide what the function should do on an input of a given size (n)
//   2) Develop a recursive step:
//      Determine how to build an answer of a given size based on
//      the result of an input of smaller size (often n - 1 or n/2, etc.)
//      You need to "assume" the call on the smaller input just works.
//   3) Develop the base cases:
//      Identify the smallest input sizes for which the recursive step
//      cannot be applied, and implement them individually/separately
def factorial(n: Int): Int = {
  if (n == 0) 1 else n * factorial(n - 1)
}
factorial(10)

def hanoi[A](n: Int, from: A, middle: A, to: A): Unit =
  if (n > 0) {
    hanoi(n - 1, from, to, middle)
    print(s"$from -> $to; ")
    hanoi(n - 1, middle, from, to)
  }

hanoi(1, 'L', 'M', 'R')
hanoi(2, 'L', 'M', 'R')
hanoi(3, 'L', 'M', 'R')
hanoi(4, 'L', 'M', 'R')

// Here's how you might write something to determine if
// the number 0 is in a list, using if/else statements
def find0(list: List[Int]): Boolean =
  if (list.isEmpty) false
  else if (list.head == 0) true else find0(list.tail)

find0(List(1,2,3))
find0(List(1,2,0,3))

// Note that you can get rid of the if/else because
// we're looking for a true/false answer anyway
// (lazy evaluation)... Now we're looking for 1's
def find1(list: List[Int]): Boolean =
  list.nonEmpty && (list.head == 1 || find1(list.tail))

find1(List(0,2,3))
find1(List(0,1,2,3))

// Here's breaking up the list using match/case
// (Looking for 2's)
def find2(list: List[Int]): Boolean = list match {
  case Nil => false
  case h :: t => h == 2 || find2(t)
}

find2(List(0,1,3))
find2(List(0,1,2,3))

// Here's another way to break it up that looks
// a bit cleaner (note the base case that returns true)
// (Looking for 3's)
def find3(list: List[Int]): Boolean = list match {
  case Nil => false
  case 3 :: _ => true
  case _ :: t => find3(t)
}

find3(List(0,1,2))
find3(List(0,1,2,3))

// But most of these are toy examples. Except
// for the Towers of Hanoi problem, they are
// not any better than writing loops.
// Some algorithms and recursive data structures
// do really benefit from recursive functions