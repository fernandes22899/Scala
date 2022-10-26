package cs671

/**
  * This package defines ad hoc case classes for a compact, efficient representation of polynomials.
  * Implicit conversions are used so polynomial operations can be applied to numbers (e.g., `3 * p`)
  * for convenience.
  *
  * Polynomials can be built using `x` (a term of degree 1 with coefficient 1), implicit conversions
  * and standard operations: `3 * x @@ 2 + 2 * x + 1` is a polynomial object (where `@@`
  * is exponentiation).
  *
  * This package exposes much of the implementation for the purpose of testing and grading.  In
  * particular, constructors of the case classes are public where they shouldn't be.
  */
package object polynomials {

  /**
    * Implicit conversion.
    * This brings the polynomial operations `+`, `-` and `*` to integers.
    */
  implicit class PolynomialIntWrapper(val n: Int) extends AnyVal {
    def * (p: Polynomial): Polynomial = p * n

    def + (p: Polynomial): Polynomial = p + n

    def - (p: Polynomial): Polynomial = -p + n
  }
}
