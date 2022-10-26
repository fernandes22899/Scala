package grading

import org.scalatest.funsuite.AnyFunSuite

class SampleRefTests extends AnyFunSuite {

  import cs671.practice.CountingRefImpl

  test("get (1)") {
    val ref = new CountingRefImpl(42)
    assert(ref.get() === 42)
    assert(ref.get() === 42)
  }

  test("reset (1)") {
    val ref = new CountingRefImpl("foo")
    ref.get()
    ref.reset()
    assert(ref.accessCount === 0)
    ref.get()
    assert(ref.accessCount === 1)
  }

  test("toString (1)") {
    val ref = new CountingRefImpl("string")
    assert(ref.toString === "string")
  }

  test("delete (1)") {
    val ref = new CountingRefImpl("foo")
    ref.disable()
    assertThrows[IllegalStateException](ref.get())
  }
}
