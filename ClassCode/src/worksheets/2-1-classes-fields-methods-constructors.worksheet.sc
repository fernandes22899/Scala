// Scala allows you to define classes in much the same ways
// you're familiar with, with fields (member variables) and
// methods, and varying levels of access to outside classes
// Note that the code for the "primary" constructor occurs
// right within the body of the class.
class Car {
  println("car created")

  private var speed: Int = 0

  def drive(): Unit = println(s"Vroooooom! at $speed mph")
  def accelerate(): Unit = speed += 1
  def stop(): Unit = speed = 0
}

// Example of creating an instance of our class and using it
val myRolls: Car = new Car()
myRolls.accelerate()
myRolls.drive()
myRolls.accelerate()
myRolls.drive()
//myRolls.speed = -42

// Note that we haven't overloaded equality, so no two instances
// are automatically equal
val otherCar = new Car
myRolls == otherCar


// Scala allows additional constructors (called "this"), but
// they all must start by calling a constructor of the class
// Note that the primary constructor here is private
// Also note that anything declared with val/var in primary
// constructor argument list is treated as a field
class Horse private (val name: String, private var num: Int) {
  def this(name: String) = this(name, 0)

  def run(): Unit = println(s"horse $name running")

  def number: Int = num
  def setNumber(n: Int): Unit = {
    require(n > 0)
    num = n
  }

  // ...or you can use notation with underscore for setter
  def number_=(n:Int): Unit = {
    require(n > 0)
    num = n
  }
}

// We can only call the one-argument consructor
val h = new Horse("Secretariat")
h.run()
h.name
//new Horse("H", -10)
//h.number = -10
h.setNumber(3)
h.number
h.number = 6
h.number

// Scala has a special kind of class called a case class
// * Instances don't need "new" to be created
// * All constructor parameters are treated as public val's
// * toString, ==, and hashcode are automatically overloaded
case class RaceCar(color: String, number : Int) {
  def drive(): Unit = println(s"$color car $number racing!")
}

val r = RaceCar("red", 10)
r.drive()
r.color
r.toString

val r1 = RaceCar("red", 10)
r == r1

// Case classes are really handy to match against, as cases
r match {
  case RaceCar(_, 1) => "first car"
  case RaceCar(c, _) => c
}

// Best practice: Don't introduce new fields nor
//                auxiliary constructors to case classes