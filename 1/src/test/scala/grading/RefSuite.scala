package grading

import edu.unh.cs.mc.grading.GradingRun

class RefSuite extends SampleRefTests with GradingRun {

  import cs671.practice.CountingRefImpl

  test("get (2)") {
    val value = new Object
    val ref   = new CountingRefImpl(value)
    assert(ref.get() eq value)
    assert(ref.get() eq value)
  }

  test("accessCount (1)") {
    val ref = new CountingRefImpl("zero")
    assert(ref.accessCount === 0)
  }

  test("accessCount (2)") {
    val ref = new CountingRefImpl("foo")
    ref.get()
    assert(ref.accessCount === 1)
    ref.get()
    assert(ref.accessCount === 2)
  }

  test("reset (2)") {
    val ref = new CountingRefImpl("foo")
    ref.reset()
    ref.get()
    ref.reset()
    ref.reset()
    assert(ref.accessCount === 0)
    ref.get()
    assert(ref.accessCount === 1)
    ref.reset()
    assert(ref.accessCount === 0)
    ref.get()
    ref.get()
    assert(ref.accessCount === 2)
  }

  test("toString (2)") {
    val b   = new StringBuilder("A")
    val ref = new CountingRefImpl(b)
    assert(ref.toString === "A")
    b += 'B'
    assert(ref.toString === "AB")
  }

  test("nulls (1)") {
    assertThrows[IllegalArgumentException](new CountingRefImpl(null))
  }

  test("delete (2)") {
    val ref = new CountingRefImpl("foo")
    ref.disable()
    assertThrows[IllegalStateException](ref.reset())
  }

  test("delete (3)") {
    val ref = new CountingRefImpl("foo")
    ref.disable()
    assertThrows[IllegalStateException](ref.accessCount)
  }

  test("delete (4)") {
    val ref = new CountingRefImpl("foo")
    ref.disable()
    ref.disable()
  }

  test("delete (5)") {
    val ref = new CountingRefImpl("foo")
    ref.disable()
    assert(ref.toString === "foo")
  }

  test("various (1) [5pts]") {
    val value = new Object
    val ref   = new CountingRefImpl(value)
    assert(ref.accessCount === 0)
    assert(ref.get() eq value)
    assert(ref.accessCount === 1)
    assert(ref.get() eq value)
    assert(ref.accessCount === 2)
    ref.reset()
    assert(ref.accessCount === 0)
    assert(ref.get() eq value)
    assert(ref.accessCount === 1)
    ref.disable()
    ref.disable()
    assertThrows[IllegalStateException](ref.get())
    assertThrows[IllegalStateException](ref.reset())
    assertThrows[IllegalStateException](ref.accessCount)
    assert(ref.toString === value.toString)
  }
}
