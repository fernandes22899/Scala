package parallel

/**
  * A simple semaphore.  It is a `java.util.concurrent.Semaphore` in ''unfair'' mode.
  *
  * Instances of this class are ''thread-safe''.
  *
  * @see `java.util.concurrent.Semaphore`
  *
  * @param n the initial number of permits
  */
class JavaSem(n: Int) extends Semaphore {

  private[this] val sem = new java.util.concurrent.Semaphore(n)

  def acquire(): Unit = sem.acquire()

  def release(): Unit = sem.release()
}
