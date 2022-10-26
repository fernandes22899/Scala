// Here's an example of making a mutable
// structure that is thread-safe. Note that
// if there are multiple methods that can
// modify the state of the object, they all
// should be protected in this way so they
// cannot interfere with each other.
import org.tinyscalautils.Controls.Times
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }
import scala.collection.mutable.ArrayBuffer

class SafeBuffer {
    private val buffer = ArrayBuffer.empty[String]

    //Note that locking on a private variable makes it so
    //nothing external can block on the same object
    //Always use val for the synchronized object so that
    //it doesn't get swapped out from underneath us!    
    def += (s: String): Unit = buffer.synchronized {
      buffer += s
    }

    //Have to synchronize around getters with the
    //Java Virtual Machine because it can cache
    //values and not go looking again until the next
    //synchronization.
    def size: Int = buffer.synchronized(buffer.size)
}

  var shared = new SafeBuffer()

class Adder(n: Int, s: String) extends Runnable {

  def run(): Unit = {
    n times {
      shared += s
    }
  }
}

for (_ <- 1 to 20) {
  shared = new SafeBuffer()
  
  val r1 = new Adder(5000, "foo")
  val r2 = new Adder(5000, "bar")

  val t1 = new Thread(r1, "T1")
  val t2 = new Thread(r2, "T2")
  t1.start()
  t2.start()

  t1.join()
  t2.join()

  println(shared.size)
}

// Note that immutable data structres (like List)
// are inherently thread safe. The data is only
// being read, not updated.
