import scala.collection.mutable

// The following code follows the example
// in the HTML book, Chapter 18
trait Publication {
  def title: String
}

case class Book(title: String) extends Publication
case class Magazine(title: String) extends Publication

val b1 = Book("A")
val b2 = Book("B")
val books: mutable.Set[Book] = mutable.Set(b1, b2)

// Now that we have a set of books, what if we
// want to write a method that could print
// out the titles of all the books in our
// Set? Maybe we would write it more
// generally so it could work on all Publications
def printTitles1(collection: mutable.Set[Publication]) =
  for (p <- collection)
    println(p.title)

// Here's a similar method for List
def printTitles(collection: List[Publication]) =
  for (p <- collection)
    println(p.title)

//It would work for the List versions...
val bookList: List[Book] = List(b1, b2)

printTitles(bookList)

// But not for the mutable map.
//printTitles1(books)

// The code above generates the following compile-time error:
//type mismatch;
//found   : scala.collection.mutable.Set[Book]
//required: scala.collection.mutable.Set[Publication]
//Note: Book <: Publication, but trait Set is invariant in type A.
//You may wish to investigate a wildcard type such as `_ <: Publication`. (SLS 3.2.10)

// This is because as far as the compiler
// knows, the code inside our function
// could be trying to add other kinds
// of Publications to our Set (like
// magazines), but our Set isn't
// guaranteed to be able to handle them.
// This leads us to want a way to specify
// that we won't be doing that sort of thing.

