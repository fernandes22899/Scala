package cs671.polynomials

object Polynomial {
  def apply(n: Int): Polynomial = C(n)
  val x: Polynomial = S(0, C(1)) // replace null with correct implementation
}

sealed abstract class Polynomial {
  def degree: Int
  def eval(x: Double): Double
  def + (that: Polynomial): Polynomial
  def + (n: Int): Polynomial
  def - (that: Polynomial): Polynomial
  def - (n: Int): Polynomial
  def * (that: Polynomial): Polynomial
  def * (n: Int): Polynomial

  @throws[IllegalArgumentException]("if n is negative")
  def @@ (n: Int): Polynomial
  def unary_- : Polynomial
  def derivative: Polynomial
}

case class C(c: Int) extends Polynomial {
  def degree: Int                      = 0
  def eval(x: Double): Double          = c
  def + (that: Polynomial): Polynomial = that match {
    case C(num) => C(c + num)
  }
  def + (n: Int): Polynomial           = C(c + n)
  def - (that: Polynomial): Polynomial = C(c) + (-that)
  def - (n: Int): Polynomial           = C(n - c)
  def * (that: Polynomial): Polynomial = that match {
    case C(num) => C(c * num)
  }
  def * (n: Int): Polynomial           = C(c * n)
  def @@ (n: Int): Polynomial          = n match {
    case 0 => C(1)
    case _ if n < 0 => throw new IllegalArgumentException("n cannot be negative")
    case _ => C(c) * (C(c) @@ (n-1))
  }
  def unary_- : Polynomial             = C(-c)
  def derivative: Polynomial           = C(0)//constant derivative is 0
}

case class X(p: Polynomial) extends Polynomial {
  def degree: Int                      = p.degree + 1
  def eval(x: Double): Double          = x * p.eval(x)
  def + (that: Polynomial): Polynomial = ???
  def + (n: Int): Polynomial           = {
    if(n == 0) X(p)
    else S(n, p)
  }
  def - (that: Polynomial): Polynomial = X(p) + (-that)
  def - (n: Int): Polynomial           = X(p) + (-n)
  def * (that: Polynomial): Polynomial = ???
  def * (n: Int): Polynomial           = ???
  def @@ (n: Int): Polynomial          = n match {
    case 0 => C(1)
    case _ if n < 0 => throw new IllegalArgumentException("n cannot be negative")
    case _ => X(p) * (X(p) @@ (n-1))
  }
  def unary_- : Polynomial             = X(-p)
  def derivative: Polynomial           = p + X(p.derivative)
}

case class S(c: Int, p: Polynomial) extends Polynomial {
  def degree: Int                      = p.degree + 1
  def eval(x: Double): Double          = c + (x * p.eval(x))
  def + (that: Polynomial): Polynomial = ???//that + (c + (Polynomial.x * p))
  def + (n: Int): Polynomial           = {
    if(n + c == 0) X(p)
    else S(n + c, p)
  }
  def - (that: Polynomial): Polynomial = S(c,p) + (-that)
  def - (n: Int): Polynomial           = {
    if(n - c == 0) X(p)
    else S(n - c, p)
  }
  def * (that: Polynomial): Polynomial = ???
  def * (n: Int): Polynomial           = ???
  def @@ (n: Int): Polynomial          = n match {
    case 0 => C(1)
    case _ if n < 0 => throw new IllegalArgumentException("n cannot be negative")
    case _ => S(c,p) * (S(c,p) @@ (n-1))
  }
  def unary_- : Polynomial             = S(-c, -p)
  def derivative: Polynomial           = p + X(p.derivative)
}
