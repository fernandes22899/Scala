package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalactic.TimesOnInt._
import org.scalactic.Tolerance._
import org.scalatest.tagobjects.Slow
import org.scalatest.time.SpanSugar._

import scala.annotation.tailrec
import scala.util.Random

class PolynomialSuite extends SamplePolynomialTests with RandomPolynomials with GradingRun {

  import cs671.polynomials.Polynomial.x
  import cs671.polynomials.{ C, Polynomial, S, X }

  override val longTimeLimit = 10.seconds

  val epsilon = 1e-5

  val zero = C(0)
  val one  = C(1)

  @tailrec
  final def isCanonical(poly: Polynomial): Boolean = poly match {
    case S(0, _) | S(_, C(0)) | X(C(0)) => false
    case C(_)                           => true
    case S(_, p)                        => isCanonical(p)
    case X(p)                           => isCanonical(p)
  }

  lazy val p3 = 3 * x + 1
  lazy val p4 = x @@ 3 - 2 * x + 5
  lazy val p5 = (x - 1) * (x - 2) * (x - 3) * (x - 4) * (x - 5)

  test("p1") {
    assert(p3 === S(1, C(3)))
  }

  test("p2") {
    assert(p4 === S(5, S(-2, X(C(1)))))
  }

  test("p3") {
    assert(p5 === S(-120, S(274, S(-225, S(85, S(-15, C(1)))))))
  }

  test("5 * x @@ 5") {
    assert(5 * x @@ 5 === X(X(X(X(X(C(5)))))))
  }

  test("5 * x @@ 5 + 5") {
    assert(5 * x @@ 5 + 5 === S(5, X(X(X(X(C(5)))))))
  }

  test("eval 1") {
    assert(zero.eval(10.0) === 0.0 +- epsilon)
  }

  test("eval 2") {
    assert(p3.eval(10.0) === 31.0 +- epsilon)
  }

  test("eval 3") {
    assert(p4.eval(10.0) === 985.0 +- epsilon)
  }

  test("eval 4") {
    assert(p5.eval(10.0) === 15120.0 +- epsilon)
  }

  test("eval 5") {
    assert(p5.eval(1.0) === 0.0 +- epsilon)
  }

  test("eval 6") {
    assert(p5.eval(2.0) === 0.0 +- epsilon)
  }

  test("eval 7") {
    assert(p5.eval(3.0) === 0.0 +- epsilon)
  }

  test("eval 8") {
    assert(p5.eval(4.0) === 0.0 +- epsilon)
  }

  test("eval 9") {
    assert(p5.eval(5.0) === 0.0 +- epsilon)
  }

  test("eval 10") {
    assert(((x - 2) @@ 20).eval(2.0) === 0.0 +- epsilon)
  }

  test("degree 1") {
    assert(zero.degree === 0)
  }

  test("degree 2") {
    assert(x.degree === 1)
  }

  test("degree 3") {
    assert(p3.degree === 1)
  }

  test("degree 4") {
    assert(p4.degree === 3)
  }

  test("degree 5") {
    assert(p5.degree === 5)
  }

  test("degree 6") {
    assert(((x + 1) @@ 30).degree === 30)
  }

  test("* zero 1") {
    assert(x * 0 === zero)
  }

  test("* zero 2") {
    assert(x * zero === zero)
  }

  test("* zero 3") {
    assert(zero * x === zero)
  }

  test("* zero 4") {
    assert(p4 * 0 === zero)
  }

  test("* zero 5") {
    assert(p4 * zero === zero)
  }

  test("* zero 6") {
    assert(zero * p4 === zero)
  }

  test("+ 1") {
    assert(zero + zero === zero)
  }

  test("+ 2") {
    assert(x + zero === x)
  }

  test("+ 3") {
    assert(zero + x === x)
  }

  test("+ 4") {
    assert(zero + x === x)
  }

  test("+ 5") {
    assert(p3 + p4 === p4 + p3)
  }

  test("+ 6") {
    assert(p3 + p4 + p5 === p5 + p4 + p3)
  }

  test("+ 7") {
    assert(x @@ 2 + x @@ 2 === 2 * x @@ 2)
  }

  test("- 1") {
    assert(x - x === zero)
  }

  test("- 2") {
    assert(-zero === zero)
  }

  test("- 3") {
    assert(p3 - p3 === zero)
  }

  test("- 4") {
    assert(p3 - x - x - x - 1 === zero)
  }

  test("- 5") {
    assert(-(p3 - p4) === p4 - p3)
  }

  test("- 6") {
    assert(-(p3 - (p4 - p5)) === p4 - p3 - p5)
  }

  test("- 7") {
    assert(x @@ 2 - x @@ 2 === zero)
  }

  test("* 1") {
    assert(x * 1 === x)
  }

  test("* 2") {
    assert(1 * x === x)
  }

  test("* 3") {
    assert(p3 * 1 === p3)
  }

  test("* 4") {
    assert(1 * p3 === p3)
  }

  test("* 5") {
    assert(x @@ 2 * x @@ 3 === x @@ 5)
  }

  test("* 6") {
    assert(2 * x @@ 2 * 3 * x @@ 3 === 6 * x @@ 5)
  }

  test("@@ 1") {
    assert(one @@ 0 === one)
  }

  test("@@ 2") {
    assert(x @@ 0 === one)
  }

  test("@@ 3") {
    assert(zero @@ 0 === one)
  }

  test("@@ 4") {
    assert(p3 @@ 0 === one)
  }

  test("@@ 5") {
    assert(one @@ 1 === one)
  }

  test("@@ 6") {
    assert(x @@ 1 === x)
  }

  test("@@ 7") {
    assert(zero @@ 1 === zero)
  }

  test("@@ 8") {
    assert(p3 @@ 1 === p3)
  }

  test("@@ 9") {
    assert(one @@ 5 === one)
  }

  test("@@ 10") {
    assert(x @@ 5 === x * x * x * x * x)
  }

  test("@@ 11") {
    assert(zero @@ 5 === zero)
  }

  test("@@ 12") {
    assert((x + 1) @@ 3 === x @@ 3 + 3 * x @@ 2 + 3 * x + 1)
  }

  test("@@ 13") {
    assert((x @@ 3) @@ 4 === x @@ 12)
  }

  test("derivative 1") {
    assert(zero.derivative === zero)
  }

  test("derivative 2") {
    assert(one.derivative === zero)
  }

  test("derivative 3") {
    assert(x.derivative === one)
  }

  test("derivative 4") {
    assert(p3.derivative === C(3))
  }

  test("derivative 5") {
    assert(p4.derivative === 3 * x @@ 2 - 2)
  }

  test("derivative 6") {
    assert((x @@ 30).derivative === 30 * x @@ 29)
  }

  test("combined 1") {
    assert((x - 5) @@ 2 === x @@ 2 - 10 * x + 25)
  }

  test("combined 2") {
    assert((x - 5) * (x + 5) === x @@ 2 - 25)
  }

  test("combined 3") {
    assert(p3 * p4 === 3 * x @@ 4 + x @@ 3 - 6 * x @@ 2 + 13 * x + 5)
  }

  test("combined 4") {
    assert(
      p3 * p5 ===
        3 * x @@ 6 - 44 * x @@ 5 + 240 * x @@ 4 - 590 * x @@ 3 + 597 * x @@ 2 - 86 * x - 120
    )
  }

  test("combined 5") {
    assert(
      p3 * p4 * p5 ===
        3 * x @@ 9 - 44 * x @@ 8 + 234 * x @@ 7 - 487 * x @@ 6 - 103 * x @@ 5
        + 2294 * x @@ 4 - 4264 * x @@ 3 + 3157 * x @@ 2 - 190 * x - 600
    )
  }

  test("combined 6") {
    assert(
      p4 @@ 5 === x @@ 15 - 10 * x @@ 13 + 25 * x @@ 12 + 40 * x @@ 11 - 200 * x @@ 10
        + 170 * x @@ 9 + 600 * x @@ 8 - 1420 * x @@ 7 + 450 * x @@ 6 + 2968 * x @@ 5
        - 4600 * x @@ 4 + 1125 * x @@ 3 + 5000 * x @@ 2 - 6250 * x + 3125
    )
  }

  test("combined 7") {
    assert((p3 * p4).degree === 4)
    assert((p3 * p5).degree === 6)
    assert((p3 * p4 * p5).degree === 9)
    assert((p4 @@ 5).degree === 15)
  }

  test("combined 8") {
    assert(p5.derivative === 5 * x @@ 4 - 60 * x @@ 3 + 255 * x @@ 2 - 450 * x + 274)
  }

  test("combined 9") {
    assert(p5.derivative.derivative === 20 * x @@ 3 - 180 * x @@ 2 + 510 * x - 450)
  }

  test("combined 10") {
    assert(p5.derivative.derivative.derivative === 60 * x @@ 2 - 360 * x + 510)
  }

  test("combined 11") {
    assert(p5.derivative.derivative.derivative.derivative === 120 * x - 360)
  }

  test("combined 12") {
    assert(p5.derivative.derivative.derivative.derivative.derivative === C(120))
  }

  test("combined 13") {
    assert(p5.derivative.derivative.derivative.derivative.derivative.derivative === zero)
  }

  test("combined 14") {
    assert(p5 === x @@ 5 - 15 * x @@ 4 + 85 * x @@ 3 - 225 * x @@ 2 + 274 * x - 120)
  }

  test("combined 15") {
    assert(x @@ 100 + 2 * x - x - x @@ 100 === x)
  }

  test("polynomials are canonical [3pts]") {
    assert(isCanonical(x))
    assert(isCanonical(zero))
    assert(isCanonical(one))
    assert(isCanonical(p3))
    assert(isCanonical(p4))
    assert(isCanonical(p5))
    assert(isCanonical(p3 + p4))
    assert(isCanonical(p3 * p4))
    assert(isCanonical(p3 + p4 + p5))
    assert(isCanonical(p3 * p4 * p5))
    assert(isCanonical(p4 - p3))
    assert(isCanonical(p4 - p3 - p5))
    assert(isCanonical(p4 @@ 5))
    assert(isCanonical((x - 2) @@ 20))
    assert(isCanonical((x + 1) @@ 30))
    assert(isCanonical((x + 1) @@ 3))
    assert(isCanonical(x @@ 12))
    assert(isCanonical(2 * x @@ 2))
    assert(isCanonical(x @@ 5))
    assert(isCanonical(6 * x @@ 5))
    assert(isCanonical(C(3)))
    assert(isCanonical(3 * x @@ 2 - 2))
    assert(isCanonical(30 * x @@ 29))
    assert(isCanonical((x - 5) @@ 2))
    assert(isCanonical((x - 5) * (x + 5)))
    assert(isCanonical(5 * x @@ 4 - 60 * x @@ 3 + 255 * x @@ 2 - 450 * x + 274))
    assert(isCanonical(20 * x @@ 3 - 180 * x @@ 2 + 510 * x - 450))
    assert(isCanonical(60 * x @@ 2 - 360 * x + 510))
    assert(isCanonical(120 * x - 360))
  }

  test("10000 random polynomials [5pts]", Slow) {
    implicit val rand = new Random(2020)
    5000 times {
      if (Thread.interrupted()) throw new InterruptedException
      val d1 = rand.nextInt(10)
      val p1 = makeRandomPolynomial(d1)
      val d2 = rand.nextInt(10)
      val p2 = makeRandomPolynomial(d2)
      sanityCheck(p1, d1, p2, d2)
    }
  }
}
