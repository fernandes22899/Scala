package grading


import edu.unh.cs.mc.grading.Controls
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow

class SampleFireworksTests extends AnyFunSuite with Controls {

  import cs671.fireworks.Color.{blue, green, red}
  import cs671.fireworks.{Music, Peony, Ring, Salute}
  import cs671.fireworks._

  val epsilon = 1e-5

  val greenFlare = Flare(green)
  val greenClover = Ring("clover", green, 5.m)
  val salute = Salute()
  val testLyrics = "Check. Check. 1...2...3..."
  val testMusic = Music("Test", 5.s, 90.db, "test.mp4", testLyrics)
  val greenSequence = greenFlare + greenClover
  val greenCombo = greenFlare ! greenClover

  //Implicits
  test( "Meters implicit works and Meters print nicely [1pt]") {
    assert(s"${2.5.m}" == "2.50 m")
  }

  test( "Seconds implicit works with max, prints nicely [1pt]") {
    assert(s"${3.2.s max 5.s}" == "5.0 s")
  }

  test( "Decibels implicit works with logarithmic add, prints nicely [1pt]") {
    assert(s"${115.db + 130.db}" == "130.1 dB")
  }

  //Basic Effect rendering
  test( "Render of VisualEffect @t=0 (flare) prints properly [1pt]") {
    assert(greenFlare.render(0.0) == "<green flash> \n")
  }

  test( "Render of VisualEffect @t=1 (flare) prints properly [1pt]") {
    assert(greenFlare.render(1.0) == "<green constant star> \n")
  }

  test( "Render of VisualEffect @t=1 (peony) prints properly [1pt]") {
    assert(greenClover.render(1.0) == "<green clover of stars [5.00 m x 2.00 m]> \n")
  }

  test( "Render of VisualEffect @t=0.6 (peony) prints properly [1pt]") {
    assert(greenClover.render(0.6) == "<green clover of stars [3.00 m x 1.20 m]> \n")
  }

  test( "Render of SoundEffect @t=0 (salute) prints properly [1pt]") {
    assert(salute.render(0.0) == ">BOOM< [@ 115.0 dB] \n")
  }

  test( "Render of SoundEffect @t=0.6 (salute) prints properly [1pt]") {
    assert(salute.render(0.6) == "\n")
  }

  test( "Render of Music @t=0 prints properly [1pt]") {
    assert(testMusic.render(0.0) == s">$testLyrics< [@ 90.0 dB] \n")
  }

  test( "Render of Music @t=0.5 prints properly [1pt]") {
    assert(testMusic.render(0.5) == ">Test continues playing< [@ 90.0 dB] \n")
  }

  // Effect operations
  test ( "Duration of greenSequence (+) is 10.s [1pt]") {
    assert(greenSequence.duration == greenFlare.duration + greenClover.duration)
  }

  test ( "Duration of greenCombo (!) is 5.s [1pt]") {
    assert(greenCombo.duration == greenFlare.duration.max(greenClover.duration))
  }

  test ( "Duration of greenSequence || greenCombo is 10.s [1pt]") {
    val greenSequence = greenFlare + greenClover; //we don't want to change top-level greenSequence
    assert((greenSequence || greenCombo).duration == greenSequence.duration)
  }

  test ( "Render of greenSequence prints properly [1pt]") {
    assert(greenSequence.render(0.s, greenSequence.duration) ==
             greenFlare.render(0.s, greenFlare.duration) +
             greenClover.render(0.s, greenClover.duration))
  }

  test ( "Render of greenCombo prints properly [1pt]") {
    assert(greenCombo.render(0.s, greenSequence.duration) ==
           greenClover.render(0.s, greenClover.duration) +
           greenFlare.render(0.s, greenFlare.duration))
  }

  test ( "Render of greenSequence || greenCombo prints properly [1pt]") {
    val greenSequence = greenFlare + greenClover; //we don't want to change top-level greenSequence
    val s1 = (greenSequence || greenCombo).render(0.s, greenSequence.duration)
    val s2 = greenClover.render(0.s, greenClover.duration) +
      greenFlare.render(0.s, greenFlare.duration) * 2 +
      greenClover.render(0.s, greenClover.duration)
    assert(s1 == s2)
  }

  test ( "RenderVisitor properly folds list of strings [1pt]") {
    val sList = List("A", "B", "CDE", "F", "GHI")
    val result = sList.foldLeft(RenderVisitor.identity)(RenderVisitor.combine)
    assert(result == "ABCDEFGHI")
  }

  test ( "traverse with RenderVisitor produces same result as render [5pts]") {
    val greenSequence = greenFlare + greenClover; //we don't want to change top-level greenSequence
    val seq = greenSequence || greenCombo
    assert(seq.traverse(0.s, greenSequence.duration)(RenderVisitor) ==
           seq.render(0.s, greenSequence.duration))
  }

  test ( "VolumeVisitor properly filters in SoundEffects [1pt]") {
    assert(VolumeVisitor.select.isDefinedAt(greenClover) == true)
    assert(VolumeVisitor.select.isDefinedAt(testMusic) == true)
    assert(VolumeVisitor.select.isDefinedAt(Salute()) == true)
  }

  test ( "VolumeVisitor properly filters out non-SoundEffects [1pt]") {
    assert(VolumeVisitor.select.isDefinedAt(greenFlare) == false)
    assert(VolumeVisitor.select.isDefinedAt(greenCombo) == false)
  }

  test ( "VolumeVisitor properly applies to yield proper volume [1pt]") {
    assert(VolumeVisitor(testMusic, 0.s, 1.s) == 90.db)
  }

  test ( "VolumeVisitor properly folds list of volumes [1pt]") {
    val dList = List(100.db, 90.db, 80.db, 90.db, 106.db)
    val result = dList.foldLeft(VolumeVisitor.identity)(VolumeVisitor.combine)
    assert(result.toString() == 107.2.db.toString())
  }

  test ( "traverse with VolumeVisitor produces correct result [3pts]") {
    val greenSequence = greenFlare + greenClover; //we don't want to change top-level greenSequence
    val seq = greenSequence || greenCombo
    assert(seq.traverse(0.s)(VolumeVisitor).toString === greenClover.volume.toString)
    assert(seq.traverse(3.s)(VolumeVisitor) == Double.NegativeInfinity.db)
    assert(seq.traverse(5.s)(VolumeVisitor).toString == greenClover.volume.toString)
  }
  //30pts total
/*
  test("Something [0pts]") {
    val sequence = (
        Peony(red, 10.m, 15.m)
          + Ring("clover", green, 5.m)
          + 5 + Peony(blue)
          ! Salute()
      ) || //alongside
        Music("Yankee Doodle Dandy snippet", 15.s, 100.db, "ydd.mp4",
          "I'm a Yankee Doodle dandy\n" +
            "A Yankee Doodle, do or die\n" +
            "A real live nephew of my Uncle Sam\n" +
            "Born on the Fourth of July.") + greenFlare
    println("Start")
    sequence.play()
    println("End")
    println("Sound summary:")
    sequence.evaluateAt[Decibels]() () (sequence.traverse(_,_)(VolumeVisitor))
  }
*/
}
