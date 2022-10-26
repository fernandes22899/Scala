// Functions & methods are defined using "def" syntax
// Argument types must be given
// Return type must be given only if can't be properly inferred
def square(x: Double): Double = x * x
square(2.0)

def cube(x: Double) = x * x * x
cube(1.1)

// Body of the function is always an expression, can be a block
// Scala has a utility method require that
// throws IllegalArgumentException when a requirement fails. 
def shorten(msg: String, max: Int): String = {
  //if (max < 3) throw new IllegalArgumentException("max < 3")
  require(max >= 3, "max < 3")
  if (msg.length <= max) msg else msg.substring(0, max - 3) + "..."
  //Scala does support return, works like you'd expect
}

shorten("loooooooooooooooooooong", 10)
shorten("loooooooooooooooooooong", 100)
//shorten("loooooooooooooooooooong", 1)

// Not so important...
// Scala supports default values and re-ordered parameters upon call
def next(x: Int, incr: Int = 1) = x + incr

val c = next(10, 2)
val d = next(10)

//Named parameters
val e = next(10, incr=3)
val f = next(incr=4, x=10)

// Very important... Note that def can be used to create things that
// *look* like variables, but are really functions that run every time
var str = "foo"

def toUpper1: String = str.toUpperCase
val toUpper2: String = str.toUpperCase
var toUpper3: String = str.toUpperCase

str = "bar"

toUpper1
toUpper2
toUpper3