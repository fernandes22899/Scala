// Scala uses traits to indicate interfaces
trait Vehicle {
  def drive(): Unit
}

// Any object of a given trait is guaranteed to implement
// all the methods defined in the trait
def driveIt(v: Vehicle): Unit = v.drive()

// Scala uses the extends keyword to let the user
// say they're going to honor that guarantee
// Car becomes a subtype of Vehicle here
class Car extends Vehicle {
  def drive(): Unit = println("Vrooooom!")
}

val c: Car = new Car()
driveIt(c)

// Scala allows us to make one-off objects that
// override one or more methods
val specialCar = new Vehicle {
  def drive(): Unit = println("Special")
}

driveIt(specialCar)

// A more formal way of doing this is with "object"
// This creates a singleton object of the type
object TheCar extends Vehicle {
  def drive(): Unit = println("The Car")
}

driveIt(TheCar)

object Utils {
  val n = 42
  def greet(name: String): String = s"hello $name"
}

Utils.greet("Joe")
Utils.n

Math.PI
Math.sin(20)

import Utils._

greet("Jane")

// Special case: Companion objects. Singleton objects can access
// private members of classes by the same name, and the class
// use acces the singleton object, as well.
class Foo {
  def sum(x: Int): Int = x + Foo.Base
  private val n = 42
}

object Foo {
  private val Base = 100
  def sumIt(f: Foo): Int = Base + f.n
}

val f:Foo = new Foo;
f.sum(1)
Foo.sumIt(f)

// Note: Anything with a "main" that you want to invoke directly
// must be an object, rather than a class.