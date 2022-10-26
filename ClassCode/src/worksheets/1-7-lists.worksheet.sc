// Lists are a key data type in functional programming, and in Scala
// They are immutable (you can't change them, only construct new ones)
val a = List(1, 2, 3)
// Notice that List is a parameterized type (List of ____)
val s = List("red", "green", "blue")

// There are a number of useful methods
a.length
a.isEmpty
a.reverse
a.last

// Lists are recursively defined in functional languages.
// Lists are primarily accessed by the front/head,
// so there's the head element
a.head
// And then there's everything else (tail)
a.tail
a.tail.tail
a.tail.tail.tail
//a.tail.tail.tail.tail //<--Generates UnsupportedOperationException

// Note that head and tail don't change a
// Instead, returns a new list every time
a

// Empty list has no head or tail
Nil

// The fundamental construction operator is ::
// (called cons, originally from Lisp)
3 :: Nil
2 :: 3 :: Nil
1 :: 2 :: 3 :: Nil

// Any operator ending with a colon (:) is right-associative
// So above line breaks down to something like this
new ::(1, new ::(2, new ::(3, Nil)))

val b = 0 :: a
val b2 = 0 +: a
val c = a.tail

// Caution: any operation that doesn't just involve
// the head or tail is going to be O(N)
a.length
a :+ 10
a.last
a(2)
a.reverse
a.splitAt(2)
a ++ b

// Immutability leads to efficient construction of lists,
// because new construction can reference older part as its tail
   a
b