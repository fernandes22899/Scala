import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.NANOSECONDS

import edu.unh.cs.mc.utils.Printing.{ println, ThreadTimeMode }
import edu.unh.cs.mc.utils.threads.timeFuture

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

object Demo extends App {

  import parallel.{ Dispatcher, PoolWork, ThreadWork }

  val NumThreads = 4  // can be changed
  val usePool = true  // can be changed

  val inputs = args.toSeq.map(_.toDouble)

  def negate(x: Double): Double = { // spends x seconds to negate x
    println(s"worker gets $x")
    NANOSECONDS.sleep((x * 1e9).round)
    -x
  }

  implicit val exec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(NumThreads))

  val dispatcher = new Dispatcher[Double, Double](inputs.iterator)
  println("dispatcher created")

  if (usePool) new PoolWork(negate, dispatcher).start()
  else new ThreadWork(NumThreads, negate, dispatcher)(new Thread(_)).start()
  println("workers started")

  timeFuture(dispatcher.future).map {
    case (duration, values) =>
      println(values.mkString("[", ", ", "]"))
      println(f"time: $duration%.3f secs")
  }.onComplete(_ => exec.shutdown())
}
