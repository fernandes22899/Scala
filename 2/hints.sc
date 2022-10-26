def multiply(a: Int, b: Int): Int = {
  require(a >= 0 && b >= 0)

  def multBy(a: Int): Int = a match {
    case 0 => 0
    case 1 => b
    case _ =>
      val a1 = a / 3 + 1 // any a1 s.t. a1 > 0 and a1 < a
      val a2 = a - a1
      // a = a1 + a2, a1 < a, a2 < a
      multBy(a1) + multBy(a2) // uses the fact that (a1 + a2) * b = a1 * b + a2 * b
  }

  multBy(a)
}

multiply(3, 7)
multiply(7, 3)
multiply(7, 0)
multiply(0, 7)

case class Num(n: Int) {
  def unary_~ : Num = if (n == 0) this else Num(n - 1)
}

val num = Num(2)
~num
~(~num)
~(~(~num))

val l1: List[Int] = 2 :: Nil
val l2: List[Int] = 1 :: l1
l2 match {
  case h :: t => h == 1 && t == l1
  case Nil => false
}

val p1: List[Int] = ::(2, Nil)
val p2: List[Int] = ::(1, p1)
p2 match {
  case ::(h, t) => h == 1 && t == p1
  case Nil => false
}

p1 == l1
p2 == l2

// S(1,S(2,C(10))) is akin to List(1,2), which is ::(1,::(2,Nil))
