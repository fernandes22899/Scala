package cs671.practice

/**
  * A reference on some value.  This trait is of limited use and is only presented for practice.
  *  However, the concept of reference classes can be useful in general
  *  (see `SoftReference` or Java's `AtomicReference`, for instance).
  *
  * @tparam A the value type
  */
trait CountingRef[A] {
  /**
    * Access count.  This is the number of times method `get` has been called since creation (or
    * since the last reset).
    */
  @throws[IllegalStateException]("if the reference has been disabled")
  def accessCount: Int

  /** Resets access count to zero. */
  @throws[IllegalStateException]("if the reference has been disabled")
  def reset(): Unit

  /**
    * Disables the reference.  This disables methods `get`, `reset` and `accessCount`.
    * This method has no effect if the reference is already disabled.
    */
  def disable(): Unit

  /** The value inside the reference. Calling this method increases the access count by one. */
  @throws[IllegalStateException]("if the reference has been disabled")
  def get(): A

  /**
    * String representation.  This is the string representation of the value inside the reference.
    * Since the typical use of this method is in logging and debugging, it remains enabled after
    * the reference has been disabled.
    */
  def toString: String
}
