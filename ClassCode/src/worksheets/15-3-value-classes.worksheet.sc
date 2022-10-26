// Scala allows you to rename types, like we did
// with typedef in C/C++, so that the compiler
// can flag errors like swapping the account number
// with an amount in a banking application.

def deposit(accountNumber: Int, cents: Int) = {
    println(s"Okay, depositing $$${cents/100.0} in account #$accountNumber")
}

// Oops, these arguments are backwards.
// Person with account #500 is happy!
deposit(500, 123456789)

// This happens because the same underlying type
// is being used for multiple arguments. A solution
// is to use different types to indicate that values
// come from different domains (money versus
// account numbers). We're "wrapping" the Int
// type in the Money1 class.

class Money1(val cents: Int) {
  require(cents >= 0)
  override def toString = f"$$$dollars%.2f"
  def dollars = cents / 100.0
}

def deposit(accountNumber: Int, amount: Money1) = {
    println(s"Okay, depositing $amount in account #$accountNumber")
}

// Below causes type-mismatch error at compile time
//deposit(new Money1(500), 123456789)

// However, this can cause a lot of overhead in terms
// of additional storage and construction/deletion time.

// Instead, Scala offers "value classes" that are treated
// like classes at compile-time, but are converted to the
// underlying type internally. Predefined value classes
// include Int, Float, etc. We can make Money into a value
// class by extending AnyVal instead of the default Object/AnyRef.
class Money(val cents: Int) extends AnyVal {
  override def toString = f"$$$dollars%.2f"
  def dollars = cents / 100.0
}

val m = new Money(150)
val c = m.cents
val d = m.dollars

// m is really an Int, and functions are run to act like
// its methods. Value classes in Scala can only have
// a single "val" field and must have no code in the
// constructor (can't have require() for example). This
// is how Scala implements Int and Float, etc. on top
// of Java's primitive types.