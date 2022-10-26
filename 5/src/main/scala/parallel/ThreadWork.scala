package parallel

import java.util.concurrent.ThreadFactory

/**
  * A utility to process work units from a dispatcher using threads.
  * The number of threads must be at least 1.  This utility does not offer any mechanism to monitor
  * the computation or to wait for its completion.  Users can rely on the dispatcher's future
  * instead.
  *
  * Instances of this class are not intended to be shared and are ''not thread-safe''.
  *
  * @see [[Dispatcher.future]]
  * @see [[PoolWork]]
  * @param workers    the number of threads created (must be at least 1). If the number of workers is
  *                   `N`, this utility can process `N` units in parallel (in other words, all the
  *                   threads are used for processing and no thread is ''wasted'' in a master role).
  *                   Furthermore, this utility creates exactly `N` threads from the factory.
  *
  * @param work       the work function.  Note that this function is shared among all threads and should
  *                   therefore be ''thread-safe'' (functions tend to be immutable, hence thread-safe).
  *
  * @param dispatcher the dispatcher of work units.  There is no assumption on the number of units
  *                   contained in this dispatcher: It can be more, less or equal to the number of
  *                   threads.
  *
  * @param tf         a thread factory, used to created exactly `workers` threads.
  * @tparam A the type of a work unit.
  * @tparam B the type of the result of completing a work unit.
  */
//@throws[IllegalArgumentException]("if workers is less than 1")
class ThreadWork[A, B](workers: Int, work: A => B, dispatcher: Dispatcher[A, B])
                      (implicit tf: ThreadFactory) {

/**
    * Starts the computation.  `workers` threads are created and started.
    * This method returns immediately (i.e., it does not block for the duration of the computation).
    *
    * Throws `IllegalStateException` if already started.
    */
  @throws[IllegalStateException]("if already started")
  def start(): Unit = {
    var nex: Option[A] = None
    var t: List[Thread] = List()
    class Runn extends Runnable {
      def run() : Unit = {
        nex = dispatcher.next()
        while(nex.isDefined){
          dispatcher.report(work(nex.get))
          nex = dispatcher.next()
        }
      }
    }
    for(num <- 1 to workers) {
      val tex = tf.newThread(new Runn)
      t = t :+ tex
    }
    for(num <- t){num.start()}
  }
}
