package parallel

import scala.concurrent.{Future, Promise}

/**
  * A dispatcher of work units.
  * A dispatcher is created with a series of inputs, obtained from an iterator.
  * Those inputs can be retrieved by worker threads using method `next`.
  * The threads should produce one output value for each input and report it using method `report`.
  * When all inputs have been consumed, method `next` returns `None`.
  * When all outputs have been returned, the future (produced by method `future`) completes with
  * the list of all outputs, in the order in which they were received (not necessarily the order in
  * which the inputs were dispatched to workers).
  *
  * A dispatcher can be created with an optional bound, used to control concurrency:
  * If present, the bound places a limit on the number of workers actively processing units in
  * parallel.
  * Note that, when a bound is used, method `next` can be blocking.
  *
  * Instances of this class are ''thread-safe''.
  *
  * @param values the values to be dispatched
  * @param bound an optional bound used to limit concurrency
  * @tparam A an input
  * @tparam B an output
  */
class Dispatcher[A, B](values: Iterator[A], bound: Option[Int] = None) {

  var dis: Int = 0
  var res = List.newBuilder[B]
  val prom : Promise[List[B]] = Promise[List[B]]
  val boun = {if(bound.isDefined) Option(new JavaSem(bound.get)) else None}
  def disSync: Unit = dis.synchronized {dis += 1}
  object sync

  /**
    * Returns the next work unit, if any.
    * This method might block if the dispatcher was created with a semaphore and enough work unit
    * are already being worked on.  It returns `None` when all units have been dispatched.
    *
    * @return the next work unit, if any.
    */
  def next(): Option[A] = {
    var n: Option[A] = None
    sync.synchronized {
      if(values.hasNext) n = {
        disSync
        Option(values.next())
      }
      else
        None
    }
    n
  }

  /**
    * Reports the output of a work unit.
    * Each work unit should trigger one call to `report` and one call only.  If more results are
    * reported than work units have been dispatched, the method throws an `IllegalStateException`.
    *
    * @param result the output of a work unit
    * @return true if all units have been completed; in that case, `future.isCompleted` is
    *         guaranteed to be true
    */
  def report(result: B): Boolean = {
    var bool : Boolean = false
    var resSize : Int = 0
    sync.synchronized {
      resSize= res.result().size
      if(resSize + 1 > dis)
        throw new IllegalArgumentException("Too many results")
      res += result
      resSize = res.result().size
      var cif : Boolean = {resSize == dis}
      if(values.isEmpty && cif) bool = true
    }
    if(boun.isDefined) boun.get.release()
    if(bool) prom.success(res.result())
    bool
  }

/**
    * Outputs from work units, as a future.
    *
    * @return a future that will contain the results of all computations, in completion order, as a
    *         list.
    */
  def future: Future[List[B]] = prom.future
}
