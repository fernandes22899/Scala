package parallel

/**
  * A simple semaphore.
  */
trait Semaphore {
  /** Acquires a permit.  This method blocks if no permit is available. */
  def acquire(): Unit

  /**
    * Releases a permit.  This might unblock a thread blocked on `acquire`.  Any thread can
    * "release" a permit, including threads that never acquired one.
    */
  def release(): Unit
}
