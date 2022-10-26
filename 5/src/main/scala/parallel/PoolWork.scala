package parallel

import scala.concurrent.ExecutionContext

/**
  * A utility to process work units from a dispatcher using a thread pool.
  * This utility does not offer any mechanism to monitor the computation or to wait for its
  * completion.
  * Users can rely on the dispatcher's future instead.
  *
  * Instances of this class are not intended to be shared and are ''not thread-safe''.
  *
  * @see [[Dispatcher.future]]
  * @see [[ThreadWork]]
  *
  * @param work the work function. Note that this function is shared among all threads and should
  *             therefore be ''thread-safe'' (functions tend to be immutable, hence thread-safe).
  *
  * @param dispatcher the dispatcher of work units.  There is no assumption on the number of units
  *                   contained in this dispatcher: It can be more, less or equal to the number of
  *                   threads.
  *
  * @param exec a thread pool on which all work units are processed.  If the pool contains `N`
  *             workers, this utility can process `N` units in parallel (in other words, all the
  *             workers are used for processing and no worker is ''wasted'' in a master role).
  * @tparam A the type of a work unit.
  * @tparam B the type of the result of completing a work unit.
  */
class PoolWork[A, B](work: A => B, dispatcher: Dispatcher[A, B])
                    (implicit exec: ExecutionContext) {

/**
    * Starts the computation.
    * This method returns immediately (i.e., it does not block for the duration of the computation).
    *
    * Throws `IllegalStateException` if already started.
    */
  @throws[IllegalStateException]("if already started")
  def start(): Unit = {
    var nex: Option[A] = None
    class Runn(op: Option[A]) extends Runnable {
      def run(): Unit = {
        dispatcher.report(work(op.get))
      }
    }
    nex = dispatcher.next()
    while(nex.isDefined) {
      exec.execute(new Runn(nex))
      nex = dispatcher.next()
    }
  }
}
