// Scala Sets & Maps default to immutable, lots of handy methods
val a = Set("Java", "C", "C++", "Python")
a.size
// (and contains if very fast)
a.contains("C")
a contains "C"

val b = a + "Scala"
b.size
a.size

a subsetOf b
b diff a
(a union b) == b
(b - "Scala") == a

// Here's how to default to mutable sets (IntelliJ shows type better)
import scala.collection.mutable.Set

val ma = Set("Java", "C", "C++", "Python")

import scala.collection.mutable

val mb = mutable.Set("Java", "C", "C++", "Python")

// Or if you want to rename a type to make things clear...
// Rename the import (in this case to MutableSet)
import scala.collection.mutable.{ Set => MutableSet }

val mc = MutableSet("Java", "C", "C++", "Python")

// Mutable sets work more like Java's sets.
mc += "Scala"
mc.size

// And here are Scala Map's
val m = Map(520 -> "C", 735 -> "Java")
m + (671 -> "Scala")
m - 520
m contains 520
m(520)
m.getOrElse(671, "taught in Scala")

val mm = mutable.Map(520 -> "C", 735 -> "Java")
mm += (671 -> "Scala")
mm.size

// Conversions to immutable versions
ma.toSet
mm.toMap
mm.toList
// Other way around must go through Sequence
//m.to(mutable.Map)
mutable.Map(m.toSeq: _*)

// This is because Maps can also be seen as sequences of pairs
m.toSet

// Note how using a Java map uses options, but isn't as easy to use otherwise
val m2 = new java.util.HashMap[String,String]
//m2 + ("this" -> "Fails")
m2.put("foo", "X")
m2.get("foo")
m2.get("bar")
Option(m2.get("foo"))
Option(m2.get("bar"))


