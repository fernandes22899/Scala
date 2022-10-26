// Before we get to type bounds, let's
// talk about type covariance first.
trait Publication {
  def title: String
}

case class Book(title: String) extends Publication
case class Magazine(title: String) extends Publication

// The + notation on a type parameter says
// that the type is covariant, and would
// allow Ref's of subtypes of A to work
// in place of this class. It also means
// we aren't allowed to ask for type A in
// any arguments (a contravariant position)...
// we can only have them be return values
// (covariant position).
class Ref[+A](value: A) {
  def get: A = value
  def count: Int = 0

  //def contains(x: A): Boolean = x == value
}

val b1 = Book("A")
val r1: Ref[Book] = new Ref(b1)

// So because Ref is defined as covariant on A,
// the following is ok.
val r2: Ref[Publication] = r1

// There's also the - notation for indicating
// contravariance. This means that TrashCan's
// of supertypes of A can work in place of
// this class. It also means we can take type
// A for a parameter type, but not for a
// return type.
class TrashCan[-A] {
  def trash(x: A): Unit = println(s"trashing $x")
  //def get: A = ???
}

val t1: TrashCan[Publication] = new TrashCan[Publication]
val t2: TrashCan[Book] = t1

//Rules of variance ("Beginning Scala" Layka & Pollak, p. 143):
// - Mutable containers should be invariant, since the
//      container-user could change the types under us
// - Immutable containers should be covariant, since
//      a collection of subtypes can be treated as a
//      collection of supertypes without penalty
// - Transformations/functions should have...
//    * Inputs that are contravariant, so that
//      transformations still work on more-general objects
//    * Outputs that are covariant, so that more specific
//      outputs can still satisfy more-general output
//      requirements