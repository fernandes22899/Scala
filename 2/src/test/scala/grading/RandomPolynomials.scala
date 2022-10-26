package grading

import org.scalactic.TimesOnInt._
import org.scalactic.Tolerance._

import scala.annotation.tailrec
import scala.util.Random

trait RandomPolynomials { self: PolynomialSuite =>

  import cs671.polynomials.Polynomial
  import cs671.polynomials.Polynomial.x

  def makeRandomPolynomial(degree: Int)(implicit rand: Random): Polynomial = {
    require(degree >= 0)
    var p = Polynomial(rand.nextInt(99) + 1)
    degree times {
      p = x * p + rand.nextInt(100)
    }
    p
  } ensuring (p => p.degree == degree && p != zero)

  @tailrec
  private def derivativeChecks(p: Polynomial, degree: Int): Unit =
    if (degree < 0) assert(p === zero)
    else {
      assert(p.degree === degree)
      derivativeChecks(p.derivative, degree - 1)
    }

  def sanityCheck(p1: Polynomial, d1: Int, p2: Polynomial, d2: Int): Unit = {
    assert(isCanonical(p1))
    assert(isCanonical(p2))
    derivativeChecks(p1, d1)
    derivativeChecks(p2, d2)

    val `p1 + p2` = p1 + p2
    assert(isCanonical(`p1 + p2`))
    assert(`p1 + p2`.degree === (d1 max d2))
    assert(`p1 + p2` === p2 + p1)

    val `p1 * p2` = p1 * p2
    assert(isCanonical(`p1 * p2`))
    assert(`p1 * p2`.degree === d1 + d2)
    assert(`p1 * p2` === p2 * p1)

    val `p1 + p1` = p1 + p1
    assert(isCanonical(`p1 + p1`))
    assert(`p1 + p1` === p1 * 2)

    val `p2 + p2` = p2 + p2
    assert(isCanonical(`p2 + p2`))
    assert(`p2 + p2` === p2 * 2)

    val `p1 - p2` = p1 - p2
    assert(isCanonical(`p1 - p2`))
    assert(`p1 - p2` === -(p2 - p1))

    val `p1 * p2 - p2` = `p1 * p2` - p2
    assert(isCanonical(`p1 * p2 - p2`))
    assert(`p1 * p2 - p2` === (p1 - 1) * p2)

    val `p1 * p2 - p1` = `p1 * p2` - p1
    assert(isCanonical(`p1 * p2 - p1`))
    assert(`p1 * p2 - p1` === p1 * (p2 - 1))

    val `(p1 + p2) * (p1 + p2)` = `p1 + p2` * `p1 + p2`
    assert(isCanonical(`(p1 + p2) * (p1 + p2)`))
    assert(`(p1 + p2) * (p1 + p2)` === p1 @@ 2 + 2 * p1 * p2 + p2 @@ 2)

    val `(p1 + p2) @@ 2` = `p1 + p2` @@ 2
    assert(isCanonical(`(p1 + p2) @@ 2`))
    assert(`(p1 + p2) @@ 2` === p1 @@ 2 + 2 * p1 * p2 + p2 @@ 2)

    val `(p1 + p2) @@ 3` = `p1 + p2` @@ 3
    assert(isCanonical(`(p1 + p2) @@ 3`))
    assert(`(p1 + p2) @@ 3` === p1 @@ 3 + 3 * p1 @@ 2 * p2 + 3 * p1 * p2 @@ 2 + p2 @@ 3)

    val `(p1 + p2) * (p1 + p2) * (p1 + p2)` = `(p1 + p2) * (p1 + p2)` * `p1 + p2`
    assert(isCanonical(`(p1 + p2) * (p1 + p2) * (p1 + p2)`))
    assert(
      `(p1 + p2) * (p1 + p2) * (p1 + p2)` === p1 @@ 3 + 3 * p1 @@ 2 * p2 + 3 * p1 * p2 @@ 2 + p2 @@ 3
    )

    assert(p1 - p1 === zero)
    assert(p2 - p2 === zero)
    assert(2 * p1 - p1 === p1)
    assert(2 * p2 - p2 === p2)

    val v1 = p1.eval(2.0)
    val v2 = p2.eval(2.0)
    assert(`p1 + p2`.eval(2.0) === v1 + v2 +- epsilon)
    assert(`p1 * p2`.eval(2.0) === v1 * v2 +- epsilon)

    val `p1 * x` = p1 * x
    assert(`p1 * x` === x * p1)
    assert(`p1 * x`.eval(2.0) === v1 * 2.0 +- epsilon)
    assert(`p1 * x`.eval(0.0) === 0.0 +- epsilon)
    assert(`p1 * x`.degree === d1 + 1)
  }
}
