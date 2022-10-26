// Let's revisit hanoi with lazy evaluation
def hanoi[A](n: Int, from: A, mid: A, to: A): LazyList[(A,A)] =
  if (n == 0) LazyList.empty
  else hanoi(n - 1, from, to, mid) #::: (from, to) #:: hanoi(n - 1, mid, from, to)

hanoi(3, 'L', 'M', 'R').toList

//We wouldn't want to make a list for 100 pieces
hanoi(100, 'L', 'M', 'R')

//But we could create an iterator from a LazyList to
//help us move through the collection
val i = hanoi(100, 'L', 'M', 'R').iterator

i.next()
i.next()
i.next()
