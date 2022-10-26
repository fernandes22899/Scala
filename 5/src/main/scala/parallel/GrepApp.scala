package parallel

import java.util.concurrent.TimeoutException
import org.scalactic.Requirements._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.util.Using
import scala.util.matching.Regex

/**
  * A parallel grep demo application.
  *
  * @param bound   an optional bound on the amount of parallelism.  Cannot be negative.  Zero means no bound.
  * @param timeout a timeout for the `find` method
  */
class GrepApp(bound: Int, timeout: Duration) {
  require(bound >= 0)

  import GrepApp.{ MatchResult, TextSource }

  /**
    * Finds source lines that contain regex matches.
    * Lines only need to contain a match (the entire line need not match the regular expression).
    * Text sources are processed in parallel (up to the specified bound)
    *
    * @param regex   the regular expression.
    * @param sources text sources.
    * @return a list of all matches, in no particular order.
    */
  def find(regex: Regex, sources: List[TextSource]): List[MatchResult] = {
    var c : Int = 0
    def reg (ts: TextSource) : List[MatchResult] = {
      c = 0
      var em : List[MatchResult] = List()
      while (ts.lines.hasNext){
        val nex: String = ts.lines.next()
        c += 1
        if(regex.findFirstIn(nex).isDefined)
          em = em :+ GrepApp.Result(ts.name,nex,c)
      }
      em
    }
    val dis = new Dispatcher[TextSource, List[MatchResult]](sources.iterator)
    val pw = new PoolWork(reg,dis)(ExecutionContext.global)
    pw.start()
    val res : List[MatchResult] = Await.result(dis.future, Duration.Inf).flatten
    res
  }
}

object GrepApp {

  /**
    * A textual source
    */
  trait TextSource {
    /** The name of the source. */
    def name: String

    /** The contents of the source. */
    def lines: Iterator[String]
  }

  /**
    * A match result
    */
  trait MatchResult {
    /** The name of the source where the line was found. */
    def name: String

    /** A line that contains a match. */
    def line: String

    /** The position of the line in the source (starts with 1). */
    def lineNumber: Int
  }

  private case class Result(name: String, line: String, lineNumber: Int) extends MatchResult

  private class URLSource(val name: String) extends TextSource {
    def lines = Using.resource(Source.fromURL(name))(_.getLines().toList).iterator
  }

  /** Command-line application. */
  def main(args: Array[String]): Unit = {

    import scala.concurrent.duration._

    val bound = args.last.toInt
    val largs = args.toList.init
    val urls = largs.init.map(new URLSource(_))
    val regex = largs.last.r

    try new GrepApp(bound, 1.minute).find(regex, urls).foreach(println)
    catch {
      case _: TimeoutException => println("timeout")
    }
  }
}
