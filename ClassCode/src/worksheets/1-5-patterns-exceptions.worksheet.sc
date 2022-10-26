// Functional languages often support some form of pattern-matching
// Scala's match-case expressions are like generalized Java/C switch statements
// Note that _ (by itself) matches anything
val x = 42
x match {
  case 0 => "zero"
  case n if n > 0 => "positive"
  case _ => "negative"
}

// You can match multiple cases using |
val arg = "-v"
val verbosity = arg match {
  case "-v" | "-V" => 1
  case "-vv" => 2
  case _ => 0
}

// Here's a different way to do it in this case, though
val verbosity2 = arg.toUpperCase match {
  case "-V" => 1
  case "-VV" => 2
  case _ => 0
}

// Try-catch statements are another form of this. Note that
// "finally" happens whether an exception is thrown/caught or not.
var ready = false

try
  if (ready) "ready" else throw new IllegalStateException("boom!")
catch {
  case e: IllegalStateException => "ex: " + e.getMessage
  case e: IllegalAccessException => "Illegal access" //scope of e is only in case
  case _: IllegalArgumentException => "bad arg"
  case _ => "some exception"
}
finally println("done")


// Cases all result in same return type, which is
// closest "ancestor" of all possible return types
// Note what happens to return type when 5 is a possible return value!
import math.Pi
val y = 5;
val v = 7;
y match {
  case 0.0 => "zero"
  case 1.0 => "one"
  case 6.0 => 5
  case `v` => "equal to v"
  case Pi => "Pi!"
  case d if d < 10 => "small"
  case y => y.toString
}