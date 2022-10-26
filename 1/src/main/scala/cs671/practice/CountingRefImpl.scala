package cs671.practice

class CountingRefImpl[A](value: A) extends CountingRef[A] {
  private var count = 0
  private var dis = 0
  def accessCount: Int = { if (dis == 1) throw new IllegalStateException
    count}
  def reset(): Unit = { if (dis == 1) throw new IllegalStateException
    count = 0 }
  def disable(): Unit = {dis = 1}
  def get(): A = { if (dis == 1) throw new IllegalStateException
    count  = count + 1
    value}
  override def toString: String = { value.toString }
}
