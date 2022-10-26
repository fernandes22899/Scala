import scala.annotation.tailrec

val nums = List(3, 2, 1, 6, 2, 1, 5, 3)

nums.sorted

// (see also sorting case study)

// How would you write a recursive function
// to insert an integer into a sorted list?
// Take a list like List(1, 4, 5, 6) and
// let's say we want to insert 3. How would
// the sorted list get pulled apart?
//   1 and List(4, 5, 6)
// In this case, we don't know if we've found
// the right location yet, so how would we
// combine the answer?
// And then, if we were to look at another
// recursive call (or a different starting
// input), we'd end up with
//   4 and List(5, 6)
// Here, we know the 3 goes before the 4
// and we can return the sorted list pretty
// easily.
// And then what happens if our number was 7
// instead of 3? What would need to be our
// base case?
def insertInSorted(x: Int, sorted: List[Int]): List[Int] = sorted match {
  case Nil => List(x)
  case min :: t =>
    if (x < min) x :: sorted else min :: insertInSorted(x, t)
}

insertInSorted(3, List(1, 4, 5, 6))

// Now, how would you go about writing
// Insertion Sort? How might you break it
// up to solve recursively? Remember that
// it takes the next number and inserts
// it in an already-sorted list (and the
// direction you process it doesn't matter)
// (sample: 3, 2, 1, 6, 2, 1, 5, 3)
def insertSort(list: List[Int]): List[Int] = list match {
  case Nil => list
  case h :: t => insertInSorted(h, insertSort(t))
}

insertSort(nums)

//...more to come...