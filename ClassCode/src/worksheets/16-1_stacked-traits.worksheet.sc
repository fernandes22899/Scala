// Here is a calculator example to show
// slightly more-complicated stacked traits

// First, here's a base-class that could be used in many situations
abstract class StackedDoer {
  //The following should be called by the "core" class
  //to invoke the stacked-override behavior before doing
  //its work--shouldn't do its work if this method returns false.
  //The point is to provide derived traits with the name of the
  //calling function and its (lone) argument, and let them say
  //whether or not to continue processing
  def checkDo(arg: Any) = {
    stackedDo(Thread.currentThread.getStackTrace()(2).getMethodName, arg)
  }

  //The following should be called by the "core" class as it
  //returns its (Int) return value
  def finishDo(arg: Int): Int = arg

  //This is expected to be overridden by traits
  def stackedDo(op: String, arg:Any): Boolean = {
    println(s"Actually Performing $op on $arg")
    true
  }
}

//Here's our calculator. Notice how it calls checkDo before its operation
//and finishDo afterward. You'll see how we use this with the stacked traits...
class Calculator(var value: Int = 0) extends StackedDoer {
  def add(x: Int): Int = {if (checkDo(x)) value += x; finishDo(value)}
  def sub(x: Int): Int = {if (checkDo(x)) value -= x; finishDo(value)}
  def mult(x: Int): Int = {if (checkDo(x)) value *= x; finishDo(value)}
  def div(x: Int): Int = {if (checkDo(x)) value /= x; finishDo(value)}
  def mod(x: Int): Int = {if (checkDo(x)) value %= x; finishDo(value)}
}

//Plain vanilla Calculator behavior
val calc = new Calculator
calc.add(5)
calc.sub(-7)

//This trait simply prints out another message
//(Point would be to send it to a file in reality)
trait Logging extends StackedDoer {
  abstract override def stackedDo(op: String, arg:Any): Boolean = {
    println(s"Logging: $op($arg)")
    super.stackedDo(op, arg)
    true
  }
}

//This trait filters out non-positive values by returning
//false when the argument is not a positive integer
trait PositiveFilter extends StackedDoer {
  abstract override def stackedDo(op: String, arg:Any): Boolean = {
    arg match {
      case arg: Int if arg > 0 =>
        println(s"Validation ok for $op: $arg > 0")
        super.stackedDo(op, arg)
      case _ =>
        println(s"Validation failed for $op: $arg is not > 0"); false
    }
  }
}

//Note how order matters--our PositiveFilter short-circuits
//further processing (although this is not always desirable...)
val calc2 = new Calculator with Logging with PositiveFilter
calc2.add(5)
calc2.sub(-7)

//Short-circuiting a little later (only skips base class println)
val calc3 = new Calculator with PositiveFilter with Logging
calc3.add(5)
calc3.sub(-7)

//Now lets add a timer--note that we have to override both
//stackedDo (to get the start time) and finishDo (to get the end time)
trait Timed extends StackedDoer {
var startTime :Long = 0
  abstract override def stackedDo(op: String, arg:Any): Boolean = {
    startTime = System.nanoTime()
    super.stackedDo(op, arg)
  }
  abstract override def finishDo(arg: Int) = {
    val totalTime: Double = (System.nanoTime() - startTime) * 1e-6
    println(s"Operation took $totalTime milliseconds")
    super.finishDo(arg)
  }
}

//Watch out! with Short-circuiting PositiveFilter, the Timed trait
//doesn't get to set a new start time--it still has the old one!
//(Could fix by not short-circuiting in PositiveFilter--call
//super's stackedDo but still return false)
val calc4 = new Calculator with Timed with Logging with PositiveFilter
calc4.add(5)
calc4.sub(-7)

//Ok here, since Timed is listed last (first in the calling order)
val calc5 = new Calculator with Logging with PositiveFilter with Timed
calc5.add(5)
calc5.sub(-7)