import scala.util.Random

val rand1, rand2 = new Random(671)
rand1.nextInt()
rand1.nextInt(26)

rand2.nextInt()
rand2.nextInt(26)

'S'.toInt
('A' + 18).toChar

val l = List("A", "B", "C")
l.take(2)
l.drop(2)
l.splitAt(2)
val (l1, l2) = l.splitAt(2)

"X" :: l  // constant time
l :+ "X"  // O(N)
l ::: l   // O(N)
l.reverse // O(N)

def sum(x: Int*) = x.sum // x has type Seq[Int]
sum(1)
sum(1, 2)
sum(1, 2, 3)
val nums = List(1, 2, 3)
sum(nums: _*)

// quick testing of assignment functions
import cs671.practice._

nextLetter(new Random(671))
makeLetters(new Random(671), 3)
