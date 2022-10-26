// There are four combinations of val/var and im/mutable collections:
// 1. val with immutable collection (can't change)
// 2. val with mutable collection (can change the contents)
// 3. var with immutable collection (can assign var with new collection)
// 4. var with mutalbe collection (there be dragons)
// In the example below, Course2 uses option 2, and Course 3 uses option 3
import scala.collection.mutable

class Course2 {
  private val students = mutable.Set.empty[String]

  def addStudent(s: String): Unit = students += s  // students .+=(s)

  def count: Int = students.size

  def allStudents: Set[String] = students.toSet
}

class Course3 {
  private var students = Set.empty[String]

  def addStudent(s: String): Unit = students += s  // students = students + s

  def count: Int = students.size

  def allStudents: Set[String] = students
}

val c2 = new Course2
c2.addStudent("Alice");
c2.addStudent("Bob");
c2.addStudent("Charlie");
c2.allStudents

val c3 = new Course3
c2.addStudent("Alice");
c2.addStudent("Bob");
c2.addStudent("Charlie");
c2.allStudents