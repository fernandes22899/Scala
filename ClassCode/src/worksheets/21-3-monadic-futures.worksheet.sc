// Scala Futures support monadic operations,
// meaning you can use foreach, map, withFilter,
// and flatMap with them to access and/or
// transform their contents. They are not collections,
// but they act in a way similar to the collections
// except that they postpone evaluation until the
// thread is complete.
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }

import scala.concurrent.Future
/** A thread pool similar to Java's CachedThreadPool, except that idle threads terminate after
 * 1 second instead of 1 minute.  It is used in demos instead of Scala's global pool because the
 * latter uses daemon threads (which leads to undesirable early termination in demos)
 */
import java.util.concurrent.{ SynchronousQueue, ThreadPoolExecutor, TimeUnit }
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

implicit lazy val global: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(
  new ThreadPoolExecutor(
    0,
    Integer.MAX_VALUE,
    1L,
    TimeUnit.SECONDS,
    new SynchronousQueue
  )
)

println("START")

def queryDB(): String = {
  println("start query")
  Thread.sleep(3000)
  println("done with query")
  "db"
}

def fetchWeather(): String = {
  println("start weather computation")
  Thread.sleep(5000)
  println("done with weather")
  "w"
}

val futW: Future[String] = Future(fetchWeather())
val db = queryDB()

//futW.foreach(w => println(s"$db + <$w>"))

for (w <- futW)
  println(s"$db + <$w>")

println("SLEEP")
Thread.sleep(6000)//appease the worksheet REPL
println("END")

// Here's an example with flatmap involed
// Don't use this with above code at the same time
// in a worksheet
val futW2: Future[String] = Future(fetchWeather())
val futDB: Future[String] = Future(queryDB())

/*
  futDB.foreach { db =>
    futW.foreach(w => println(s"$db + <$w>"))
  }
*/

for {
  db <- futDB
  w <- futW2
} println(s"$db + <$w>")

println("SLEEP")
Thread.sleep(6000)//appease the worksheet REPL
println("END")

// And here's another example with map involved  
val futP: Future[String] = for {
  w <- futW
  db <- futDB
} yield s"$db + <$w>"

futP.foreach(println)

println("SLEEP")
Thread.sleep(6000)//appease the worksheet REPL
println("END")

// And here's an example with a mix of Lists
// and Futures
val tasks: List[Future[String]] = List(futW, futDB)
val results: Future[List[String]] = Future.sequence(tasks)

val futP: Future[String] = for (List(w, db) <- results) yield s"$db + <$w>"

futP.foreach(println)

println("SLEEP")
Thread.sleep(6000)//appease the worksheet REPL
println("END")
