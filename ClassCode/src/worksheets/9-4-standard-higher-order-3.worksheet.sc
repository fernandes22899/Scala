// Next are the various flavors of "fold".
// Folding is a key concept in functional programming
// so let's try to break it down a bit. We'll start
// with a function that returns a string indicating
// what function was just called.
def f(x: String, y:String): String = s"f($x,$y)"
f("A", "B")
f("A", f("B", "C"))

val abc = List("A","B","C")

// Now see what foldLeft and foldRight do.
// They apply a function (second argument) to
// an initial value and the first item in the
// collection, then apply the function to the
// result and the second item, and so forth.
// foldLeft starts from the "left" or beginning
// of the collection, while foldRight starts
// from the "right" or end of the collection
// (assuming the collection is ordered.
abc.foldLeft("Start")(f)
abc.foldRight("Start")(f)

// Here's a numeric example, first with the
// equivalent loop.
val nums = List(1,2,3,4,5)

var acc = 10
for (x <- nums) {
  acc = 3 * acc + x + 1
}
acc

nums.foldLeft(10)((acc,x) => 3 * acc + x + 1)

// Here are ways you could write sum() and
// product() methods (note that the starting
// value is the identity for the operation
// in both cases.
nums.foldLeft(0)(_ + _)
nums.foldLeft(1)(_ * _)

// Reduce is just like fold, except that it
// takes the first item in the collection as
// the starting value, rather than a separate
// argument. Here is sum() again, as reduce
nums.reduce(_ + _)

// Has anyone heard of MapReduce with respect
// to cloud processing? Now you know the
// fundamentals behind the operations in that
// name.

// Here is another foldLeft example with a
// conversion to string involved.
nums.foldLeft("")((s,x) => s + x)

// Notice how there are multiple ways to do
// things now. Say you wanted to filter out
// the odd numbers, then halve what's left
// and sum them up. You could do it with
// filter, map, and sum, but it would generate
// other lists along the way.
nums.filter(_ % 2 == 0).map(_ / 2).sum

// fold allows you to combine these steps in
// a reasonable way. Note that it's a bit
// easier to read using a block with braces,
// as shown in the second example.
nums.foldLeft(0)((a,x) => if (x % 2 == 0) a + x / 2 else a)

nums.foldLeft(0) { (a, x) =>
  if (x % 2 == 0) a + x / 2 else a
}

// For what it's worth, groupBy is yet another
// method that takes a function as an argument,
// but in this case it generates a map with
// the result of the function as the key, and
// a collection of every input that generated
// that result as a value. This could have been
// helpful in the Mastermind program, for example.