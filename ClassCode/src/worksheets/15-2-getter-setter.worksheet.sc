// Scala implements fields using hidden getter
// and setter methods.
class C(val x: Int, var y: Int)

val c = new C(10, 15)

c.x //actually a call to a hidden method named x
c.y //a call to a hidden method named y
c.y = 42 // a call to a hidden method named y_
c.y

// Consequence 1:
// Any Scala method (specified with "def") can be
// overridden by a field (using "val" or "var")

trait T {
  def x: Int
}

class U(val x: Int) extends T
class V(var x: Int) extends T
//class W(var y: Int) extends T //Error!

// Consequence 2:
// You can create pseudo-fields using methods
// that end with _

class D(private var actual: Int) {
  def x: Int = actual
  def x_=(n: Int): Unit = {actual = n}
}

val d = new D(10) // sets actual to 10
d.x               // reads actual
d.x = 42          // sets actual to 42
d.x

// These can be more general-purpose methods,
// too. They can have side-effects and throw
// exceptions, etc.
class Item(private var name: String, private val priceInCents: Int) {
  private var currentPrice: Int = priceInCents

  def price: Int = currentPrice
  def price$: Double = price / 100.0

  def price_=(p: Int): Unit = {
    require(p >= 0)
    currentPrice = p
  }
  def price$_=(p: Double): Unit = {
    price = math.round(p * 100).toInt
  }
  ///...
}

val item = new Item("Dog food", 6340)
item.price
item.price$
item.price = 350
item.price$
//item.price$ = -4.25 //generates exception
item.price$ = 4.25
item.price