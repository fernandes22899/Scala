import cs671.polynomials.{ C, S, X }
import cs671.polynomials.Polynomial.x

val p1 = S(5, S(-1, S(3, X(X(C(2))))))

p1.degree
p1.eval(3)

val p2 = X(S(1, S(-4, C(1))))

p1 + p2
p1 - p2
-p2
p2 * 10
p1 * p2
p2 @@ 3

p1.derivative

p1 == 2 * x @@ 5 + 3 * x @@ 2 - x + 5
p2 == x @@ 3 - 4 * x @@ 2 + x

p1 - 2 * x @@ 5
