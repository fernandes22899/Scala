package cs671

/**
  * Program 4: advanced OO in Scala.
  *
  * This assignment implements a scheduling and analysis tool for developing and
  * deploying elaborate fireworks displays. It requires mixing in traits and the development
  * of a domain-specific language (DSL)
  *
  * Unless otherwise noted, invalid parameters are rejected with `IllegalArgumentException`
  * and methods called at the wrong time (if applicable) are rejected
  * with `IllegalStateException`.
  */

package object fireworks {
  class Decibels(val decibels: Double) extends AnyVal {
    def +(rhs: Decibels): Decibels = new Decibels(10 * Math.log10(Math.pow(10, decibels/10) +
      Math.pow(10, rhs.decibels/10)))
    def *(rhs: Double) = new Decibels(3 * rhs)
    override def toString() = ("%1.1f" format decibels) + " dB"
    //
  }

  //implicit Decibels from Double needed
  implicit class DecibelsFromDouble(val dec: Double) extends AnyVal {
    def db: Decibels = new Decibels(dec)
  }

  class Meters(val meters: Double = 0) extends AnyVal {
    def +(m: Meters) = new Meters(meters + m.meters)
    def -(m: Meters) = new Meters(meters - m.meters)
    def *(d: Double) = new Meters(meters * d)
    def /(m: Meters) = meters/m.meters
    override def toString() = ("%1.2f" format meters) + " m"
  }

  //implicit Meters from Double needed
  implicit class MetersFromDouble(val met: Double) extends AnyVal {
    def m: Meters = new Meters(met)
  }

  class Seconds(val seconds: Double = 0) extends AnyVal {
    def +(s: Seconds) = new Seconds(seconds + s.seconds)
    def -(s: Seconds) = new Seconds(seconds - s.seconds)
    def /(d: Double) = new Seconds(seconds / d)
    def *(d: Double) = new Seconds(seconds * d)
    def max(s: Seconds) = new Seconds(s.seconds max seconds)
    def until(s: Seconds): Range = Range(seconds.toInt, s.seconds.toInt)
    def to(s: Seconds): Range = Range(seconds.toInt, s.seconds.toInt).inclusive
    override def toString() = ("%1.1f" format seconds) + " s"
    def toInt() = seconds.toInt
    def mmss(): String = f"${seconds.toInt / 60}:${seconds % 60}%04.1f"
  }

  //The following allows us to convert to Double for
  //purposes of comparison operators, for example.
  implicit def DoubleFromSeconds(s: Seconds) = s.seconds

  //implicit Seconds from Double needed
  implicit class SecondsFromDouble(val sec: Double) extends AnyVal {
    def s: Seconds = new Seconds(sec)
  }

  /** Color enumeration for indicating the color of fireworks
   */
  object Color extends Enumeration {
    type Color = Value
    val white, red, orange, yellow, green, blue, indigo, violet, gold = Value
  }
}
