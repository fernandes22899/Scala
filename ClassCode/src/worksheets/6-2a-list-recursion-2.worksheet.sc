import scala.annotation.tailrec

val letters = List('X', 'Y', 'Z')

letters.take(2)
letters.drop(2)

def take[A](list: List[A], n: Int): List[A] = (list, n) match {
  case (_, 0) | (Nil, _) => Nil
  case (h :: t, _) => h :: take(t, n - 1)
}

take(letters, 2)

@tailrec
final def drop[A](list: List[A], n: Int): List[A] = (list, n) match {
  case (_, 0) | (Nil, _) => list
  case (_ :: tail, _) => drop(tail, n - 1)
}

drop(letters, 2)

letters ::: letters
letters :: letters

def append[A](l1: List[A], l2: List[A]): List[A] = l1 match {
  case Nil => l2
  case h1 :: t1 => h1 :: append(t1, l2)
}

append(letters, letters)

