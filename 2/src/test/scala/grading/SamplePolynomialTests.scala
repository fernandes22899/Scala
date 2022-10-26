package grading

import org.scalactic.Tolerance._
import org.scalatest.funsuite.AnyFunSuite

class SamplePolynomialTests extends AnyFunSuite {

  import cs671.polynomials.Polynomial.x
  import cs671.polynomials.{ C, Polynomial, S, X }

  val p1 = S(5, S(-1, S(3, X(X(C(2))))))
  val p2 = X(S(1, S(-4, C(1))))

  test("degree") {
    assert(p1.degree === 5)
  }

  test("eval") {
    assert(p1.eval(3.0) === 515.0 +- 1e-5)
  }

  test("+") {
    assert(p1 + p2 === S(5, X(S(-1, S(1, X(C(2)))))))
  }

  test("+ Int") {
    assert(p1 + 1 === S(6, S(-1, S(3, X(X(C(2)))))))
  }

  test("- binary") {
    assert(p1 - p2 === S(5, S(-2, S(7, S(-1, X(C(2)))))))
  }

  test("- Int") {
    assert(p1 - 5 === X(S(-1, S(3, X(X(C(2)))))))
  }

  test("- unary") {
    assert(-p2 === X(S(-1, S(4, C(-1)))))
  }

  test("* scalar") {
    assert(p2 * 10 === X(S(10, S(-40, C(10)))))
  }

  test("* polynomial") {
    assert(p1 * p2 === X(S(5, S(-21, S(12, S(-13, S(3, S(2, S(-8, C(2))))))))))
  }

  test("@@") {
    assert(p2 @@ 3 === X(X(X(S(1, S(-12, S(51, S(-88, S(51, S(-12, C(1)))))))))))
  }

  test("derivative") {
    assert(p1.derivative === S(-1, S(6, X(X(C(10))))))
  }

  test("friendly syntax 1") {
    assert(2 * x @@ 5 + 3 * x @@ 2 - x + 5 === p1)
  }

  test("friendly syntax 2") {
    assert(x @@ 3 - 4 * x @@ 2 + x === p2)
  }

  test("simplification") {
    assert(p1 - 2 * x @@ 5 === S(5, S(-1, C(3))))
  }

  test("exceptions") {
    assertThrows[IllegalArgumentException](x @@ (-1))
  }

  test("constants") {
    assert(Polynomial(42) === C(42))
  }
}
