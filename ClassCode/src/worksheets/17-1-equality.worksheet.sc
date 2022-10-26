// This example follows the example given
// in Chapter 17 of the HTML book. It
// illustrates how to overload equals and
// hashCode for proper behavior when using
// ==, ##, and anything else that uses
// these such as Set or Maps (for the keys).
class BookRef(val title: String, val author: String, val edition: Int = 1) {
  override def toString = s""""$title" by $author"""

  override def equals(obj: Any): Boolean = obj match {
    case that: BookRef => that.title == this.title && that.author == this.author
    case _ => false
  }

  override def hashCode(): Int = (title, author).##
}

val b1 = new BookRef("Moby Dick", "Herman Melville")
val b2 = new BookRef("A Christmas Carol", "Charles Dickens", 3)
val b3 = new BookRef("1984", "George Orwell")
val b4 = new BookRef("War and Peace", "Leo Tolstoy", 2)
val b5 = new BookRef("Les Miserables", "Victor Hugo", 4)

val b = new BookRef("Moby Dick", "Herman Melville", 2)

b == b1

val myBooks = Set(b1, b2, b3, b4)
myBooks contains b
(myBooks + b5) contains b
myBooks subsetOf (myBooks + b5)
(myBooks + b5 - b5) contains b
(myBooks + b5 - b5) == myBooks

b.##
b1.##
