//Implicit conversions allow you to convert one type to another
//without requiring you to make any explicit method calls

/**/
//Say we have a basic person class
class Person(val name: String, val age : Int) {
  def introduce() = {println(s"My name is $name and I am $age years old.")}
}
/**/

//If we want to be able to create a person from a
//tuple of String and Int, we need to add a function
//with the keyword "implicit" to an object, and then
//import that object
object MyImplicits {
  implicit def tuple2Person(value: (String, Int)): Person =
    new Person(value._1, value._2)

  /*
  //you could alternatively have defined the class as
  //implicit, but it has to be in an object or other class
  implicit class Person(id: (String, Int)) {
    def name = id._1
    def this(name: String, age : Int) { this((name, age)) }
    def introduce() = {println(s"My name is ${id._1} and I am ${id._2} years old.")}
  }*/
}

//Now, if I import MyImplicits, I can use the new conversion
import MyImplicits._

//Notice the grey underline in the IDE where implicit
//conversion is being used
("Bob", 22).introduce()
val bob: Person = ("Bob", 22)

//This can be useful when you want to add new behavior to a class
/**/
class MyLine(p: Person) {
  def says(what: String) = println(s"${p.name}: $what!")
  def whispers(what: String) = println(s"${p.name}: <$what>")
}
/**/

object MyOtherImplicits {
  implicit def heyString(value: Person) : MyLine = new MyLine(value)

  /*
  //you could alternatively have defined the class as
  //implicit, but it has to be in an object or other class
  implicit class MyLine2(p: Person) {
    def says(what: String) = println(s"[${p.name}] $what!")
    def whispers(what: String) = println(s"[${p.name}] <$what>")
  }
  */
}

import MyOtherImplicits._

val alice: Person = ("Alice", 24)
bob says "Hello there"
alice whispers "Shhhh"
bob says "What's wrong?"
alice whispers "You're too loud"
bob whispers "oh"

//Implicit conversions cannot be chained unless
//the conversion function takes an implicit parameter.
//See the following for more on this advanced topic:
//http://docs.scala-lang.org/tutorials/FAQ/chaining-implicits.html