// When dealing with collections, we often want to write fairly
// generic methods that don't care much about what the underlying
// type is.
def first(pair: (Any, Any)) = pair._1

// Unfortunately, defining a method like above doesn't leave
// us with the type information we would expect.
first((671, 735))

// These are invalid, because result is of type Any
//first((671, 735)) + 10
//first(("foo", "bar")).toUpperCase

// DON'T DO THIS! (avoid asInstanceOf whenever possible)
first((671, 735)).asInstanceOf[Int] + 10
first(("foo", "bar")).asInstanceOf[String].toUpperCase

// To write the function the way you're thinking, use a
// type parameter (variable name in parentheses, usually
// a single capital letter)
def firstPar[A](pair: (A, A)): A = pair._1

// Now Scala will infer the proper type, and allow you
// to use the result as that type rather than Any
firstPar((671, 735))
firstPar(("foo", "bar"))

firstPar((671, 735)) + 10
firstPar(("foo", "bar")).toUpperCase

// You can force it to the type you want, if necessary
firstPar[Int]((671, 735))
firstPar[Double]((671, 735))
firstPar[String](("foo", "bar"))

// compile-time error
//firstPar((671, 735)).toUpperCase

// runtime error
//first((671, 735)).asInstanceOf[String].toUpperCase

// But what if you want two different types...?
firstPar((671, "foo"))

// Can use multiple type parameters
def firstPar2[A,B](pair: (A, B)): A = pair._1

firstPar2((671, "foo"))
firstPar2((671, "foo")) + 10

// Java has similar syntax:
// <A,B> A first(Pair<A,B> pair) { ... }
// And you've seen this in C++ with template functions

// Notice that none of the variable names matter
def firstPar3[T,U](tuple: (T, U)): T = tuple._1
firstPar3((671, "foo"))
