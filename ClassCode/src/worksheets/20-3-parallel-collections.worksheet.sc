// Some folks have worked on ways to make
// parallel behavior occur behind the scenes.
// There are some parallel collections that
// allow you to use them more like the collections
// you're used to, but they do as much as they
// can in parallel on individual elements.
import org.tinyscalautils.Printing.{ ThreadTimeMode, println }
import org.tinyscalautils.Timing.slow

import scala.collection.parallel.CollectionConverters._
import scala.io.Source
import scala.util.Using

//noinspection SimplifiableFoldOrReduce

val urls = List.fill(10)("https://cs.unh.edu/~cs671/")

// Note that the slow function will take at
// at least 1 second (the argument passed in)
def search(url: String): Int = {
  val count = slow(1.0)(Using.resource(Source.fromURL(url))(_.length))
  println("done")
  count
}

println("START")
val results = urls.par.map(search)
println(results)
// Some of your favorite functions still
// work as expected, but might be
// supercharged to work in parallel.
println(results.sum)

// If you want to write your own sequential
// operations, you need to make sure to
// grab a sequential version of the
// collection using the seq method
var sum1 = 0
for (x <- results.seq) sum1 += x
println(sum1)

val sum2 = results.reduce(_ + _)
println(sum2)

println("END")

