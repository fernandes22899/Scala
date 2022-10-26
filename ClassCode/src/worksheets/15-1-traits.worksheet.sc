// Scala has "traits" to act a lot like interfaces
// Here's a trait for something that has a price
trait Priced {
  def price: Int
  def price$: Double = price / 100.0
  override def toString = super.toString + f" at $$${price$}%.2f"
}

// Here's a companion object to define Priced.total()
// on a collection of Priced objects 
object Priced {
  def total(items: Priced*): Double = {
    var sum = 0.0
    for (i <- items) sum += i.price$
    sum
  }
}

// Here's a trait for something with a name
// (notice extension of Ordered trait)
trait Named extends Ordered[Named] {
  def name: String
  override def toString = super.toString + ":" + name
  def compare(that: Named): Int = this.name compare that.name
}

// Now let's create a generic class that puts
// parentheses around string representation so
// we can mix in some traits and see what happens
class C {
  override def toString = "(" + super.toString + ")"
}

// See how Named and Priced are mixed into Item
// (and how super works)... 
// Note that the right-most class is the latest thing,
// and therefore the closest one to super. The base
// class (extends) is deep inside.
class Item(val id: Long, val name: String, val price: Int) extends C with Named with Priced {
  override def toString = "[" + super.toString + "]"
}

val a = new Item(42, "desk", 35000)

// Now let's see what happens when we mix in a step at a time
class Book(val title: String, val price: Int) extends Priced

class BookAdapter(title: String, p: Int) extends Book(title, p) with Named {
  def name: String = title
}

val b = new BookAdapter("Moby Dick", 1000)

// We don't need classes to do this--it could be done
// with an \"anonymous type\" instead 
val c: Book with Named = new Book("phone", 500) with Named {
  val name = title
}

a > b //'d' from desk > 'M' from Moby
a <= c //'d' from desk <= 'p' from phone

// Here are more examples with anonymous types
val cheap = new Priced {
  val price = 1
}

object FancyBook extends Named with Priced {
  val name = "Scala"
  val price = 1500
}
FancyBook

// Polymorphism is alive and well with traits
Priced.total(a,b,c,FancyBook)

// Notice how the List changes type to accomodate
a :: b :: Nil
val aList = a :: Nil
val bList = b :: Nil
aList ++ bList
val cList = c :: FancyBook :: Nil
aList ++ bList ++ cList
