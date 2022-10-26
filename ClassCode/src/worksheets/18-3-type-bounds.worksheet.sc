import scala.collection.mutable

// We don't need to rely on covariance or
// contravariance to determine what our
// methods might work with. We can also
// specify type bounds, using <: and >:
// The first means "that type or any subtype"
// while the other means "that type or
// any supertype".
trait Publication {
  def title: String
}

case class Book(title: String) extends Publication

case class Magazine(title: String) extends Publication

def printTitles[T <: Publication](collection: mutable.Set[T]) =
  for (p <- collection)
    println(p.title)

def addMagazine[T >: Magazine](collection: mutable.Set[T]) =
  collection += Magazine("M")

def printTitlesAndAddMagazine[T >: Magazine <: Publication](collection: mutable.Set[T]) = {
  for (p <- collection)
    println(p.title)
  collection += Magazine("M")
}
