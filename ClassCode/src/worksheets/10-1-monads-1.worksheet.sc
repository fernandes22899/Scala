// Monads are types that allow you to
// wrap any (more-basic) type within them, and
// provide functions that can be composed with
// each other and generate other monads (without
// "opening" the (container?) type to do it).
// Monads don't need to be traditional containers.
// In scala, the methods map, flatMap, foreach
// and withFilter are the functions chosen for
// this purpose. Lists and Options are examples
// of types that implement these are are therefore
// monadic types (monads)
List(1, 2, 3).map(x => List(x, x, x))
// We haven't seen flatMap yet, it just removes
// a layer of list (monad) from the result of a
// call to the map method
List(1, 2, 3).flatMap(x => List(x, x, x))

val num: Option[Int] = Some(42)
val nonum: Option[Int] = None

num.map(x => x + 1)
nonum.map(x => x + 1)

// Here's a function that generates an Option
// from an int, note how calls to map can
// generate what look to be odd types
def decr(x: Int): Option[Int] = if (x > 0) Some(x-1) else None

decr(10)
decr(0)

num.map(decr)
nonum.map(decr)
Some(0).map(decr)

// This is what flatMap can help with. It
// "unwraps" each result before adding it
// to the current type/"container".
num.flatMap(decr)
nonum.flatMap(decr)
Some(0).flatMap(decr)

// Note that flatMap also effectively
// removes "empty" items when you're dealing
// with putting the result into lists.
val nums = List(10, -3, 42)
nums.flatMap(decr)
nums.map(decr)

// Note how flatMap can be used to transform
// things at different stages.
val options = List(Some(10), None, Some(-3), Some(42))
options.map(o => o.map(decr))
options.map(o => o.flatMap(decr))
options.flatMap(o => o.flatMap(decr))

options.flatMap(o => o.map(decr))

