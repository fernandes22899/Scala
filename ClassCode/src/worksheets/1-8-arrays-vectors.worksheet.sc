// Scala Arrays are basically Java arrays
// (Note: VS Code worksheets gets this wrong, because arrays are mutable)
val a = Array(1, 2, 3)

a(2)
a(2) = 30
a

a.last
a.length
a.isEmpty

// Grabbing a tail and concatenation require copies,
// so they aren't constant time!
val b = a.tail
b
a(1) = 200
a
b
val c = 1 +: b

// Vectors are a middle ground between lists and arrays
val v = Vector(1, 2, 3)
v(2)
v.length
v.last
v.tail

// Use updated method to efficiently change an element
// (Doesn't make a full copy)
val w = v.updated(2, 30)
v
w

// All kinds of conversions exist
val l = a.toList
a.toVector
l.toArray
l.toVector
v.toList
v.toArray