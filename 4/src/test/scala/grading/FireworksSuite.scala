package grading

import cs671.fireworks.Color.Color
import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.time.SpanSugar._

class FireworksSuite extends SampleFireworksTests with GradingRun {

  override val longTimeLimit = 2.minutes

  import cs671.fireworks._
  import cs671.fireworks.Color.{blue, green, red}

  val redFlare = Flare(red)
  val redStar = Ring("star", red, 5.m)
  val redSequence = redFlare + redStar
  val redCombo = redFlare ! redStar

  object CountingVisitor extends EffectVisitor[Int, Effect] {
    def identity = 0
    def combine(left: Int, right: Int): Int = left + right
    def apply(effect: Effect, t: Seconds, step: Seconds): Int = {
      if (t + step >= 0 & t <= effect.duration) 1 else 0
    }
  }

  case class Fish(color: Color = Color.white, spreadDiameter: Meters = 5.m,
                   breakHeight: Meters = 20.m) extends SoundEffect with SkyVisualEffect {
    val duration = 5.s;
    val volume = 90.db
    override def shape = "swimming streaks"
    override def continueSound = s">whistle< [@ $volume] " + super.continueSound
    def spreadVertical = spreadDiameter
    def tubeSize = spreadDiameter * 0.05
    def needsMortar = true
    def weight = spreadDiameter.meters * 0.5
  }

  val fish = Fish()

  //Implicits
  test( "Meters * 3 yields proper result [1pt]") {
    assert(2.5.m * 3.0 == 7.5.m)
  }

  test( "Seconds / 5 yields proper result [1pt]") {
    assert(5.0.s / 5 == 1.s)
  }

  test( "Seconds to yields appropriate range [1pt]") {
    assert((3.s to 9.s) == (3 to 9))
  }

  test( "Decibels * 4 yields proper result [1pt]") {
    assert(115.db * 4 == 127.db)
  }

  //Basic Effect rendering
  test( "Render of VisualEffect @t=1.1 (flare) is empty [1pt]") {
    assert(greenFlare.render(1.1).isEmpty)
  }

  test( "Render of SoundEffect @t=1.2 (salute) is empty [1pt]") {
    assert(salute.render(1.2).isEmpty)
  }

  test( "Render of Music @t=1.3 is empty [1pt]") {
    assert(testMusic.render(1.3).isEmpty)
  }

  test( "Render of Fish @t=0 prints properly [2pt]") {
    assert(fish.render(0) == "<white flash> >Pop< [@ 90.0 dB] \n")
  }

  test( "Render of Fish @t=0.6 prints properly [1pt]") {
    assert(fish.render(0.6) == "<white swimming streaks of stars [3.00 m x 3.00 m]> >whistle< [@ 90.0 dB] \n")
  }

  test( "Render of Fish @t=1.0 prints properly [1pt]") {
    assert(fish.render(1.0) == "<white swimming streaks of stars [5.00 m x 5.00 m]> >whistle< [@ 90.0 dB] \n")
  }

  // Effect operations
  test ( "Duration of greenFlare + 3 is 8.s [2pt]") {
    val effect = new CompositeEffect(greenFlare) + 3
    assert(effect.duration == 8.s)
  }

  test ( "Duration of greenFlare ! 3 is 5.s [2pt]") {
    val effect = new CompositeEffect(greenFlare) ! 3
    assert(effect.duration == 5.s)
  }

  test ( "Duration of greenFlare ! 8 is 8.s [2pt]") {
    val effect = new CompositeEffect(greenFlare) ! 8
    assert(effect.duration == 8.s)
  }

  test ( "Duration of two added CompositeEffects is their sum [2pt]") {
    val effect = (redFlare + redStar) + greenSequence
    assert(effect.duration == redSequence.duration + greenSequence.duration)
  }

  test ( "Duration of CompositeEffect togetherWith another is their sum minus proper effect [2pt]") {
    val effect = (redFlare + redStar) ! greenSequence
    assert(effect.duration == redSequence.duration - redStar.duration + greenSequence.duration)
  }

  test ( "Duration of CompositeEffects alongside another is their max (1) [2pt]") {
    val effect = (redFlare + redStar) || (greenFlare + greenClover + 3)
    assert(effect.duration == greenSequence.duration + 3.s)
  }

  test ( "Duration of CompositeEffects alongside another is their max (2) [2pt]") {
    val effect = (redFlare + redStar + 3) || (greenFlare + greenClover)
    assert(effect.duration == redSequence.duration + 3.s)
  }

  test ( "Render of two added CompositeEffects is the same as all four together [3pts]") {
    val effect1 = (redFlare + redStar) + greenSequence
    val effect2 = redFlare + redStar + greenFlare + greenClover
    val times = List(0.s, 1.s, 5.s, 10.s, 11.s, 15.s, 16.s, 20.s, 21.s)
    for (t <- times) assert(effect1.render(t) == effect2.render(t))
  }

  test ( "Render of CompositeEffect togetherWith another overlaps properly [3pt]") {
    val effect1 = (redFlare + redStar) ! greenSequence
    val effect2 = redFlare + redStar ! greenFlare + greenClover
    val times = List(0.s, 1.s, 5.s, 6.s, 10.s, 11.s, 15.s, 16.s)
    for (t <- times) assert(effect1.render(t) == effect2.render(t))
  }

  test ( "Render of CompositeEffects alongside another overlaps from start [3pt]") {
    val effect1 = (redFlare + redStar) || (greenFlare + greenClover)
    val effect2 = (redFlare ! greenFlare) + redStar ! greenClover
    val times = List(0.s, 1.s, 5.s, 6.s, 10.s, 11.s)
    for (t <- times) {
      val render1 = effect1.render(t)
      val render2 = effect2.render(t)
      //println(s"assert for time $t:\n$render1  vs.  \n$render2")
      //Note that we need to compare the overall contents and not their
      //order, because the order is not preserved.
      assert(render1.sorted == render2.sorted)
    }
  }

  test ( "CountingVisitor works on two added CompositeEffects [2pts]") {
    val effect1 = (redFlare + redStar) + greenSequence
    val effect2 = redFlare + redStar + greenFlare + greenClover
    val times = List(0.s, 1.s, 5.s, 10.s, 11.s, 15.s, 16.s, 20.s, 21.s)
    for (t <- times) {
      val count1 = effect1.traverse(t)(CountingVisitor)
      val count2 = effect2.traverse(t)(CountingVisitor)
      //println(s"assert for time $t: $count1  vs. $count2")
      assert(count1 == count2)
    }
    assert(effect1.traverse(1.s)(CountingVisitor) == 1)
    assert(effect1.traverse(11.s)(CountingVisitor) == 1)
    assert(effect1.traverse(10.s, 10.s)(CountingVisitor) == 3)
    assert(effect1.traverse(step = 20.s)(CountingVisitor) == 4)
  }

  test ( "CountingVisitor works on CompositeEffect togetherWith another [2pt]") {
    val effect1 = (redFlare + redStar) ! greenSequence
    val effect2 = redFlare + redStar ! greenFlare + greenClover
    val times = List(0.s, 1.s, 5.s, 6.s, 10.s, 11.s, 15.s, 16.s)
    for (t <- times) {
      val count1 = effect1.traverse(t)(CountingVisitor)
      val count2 = effect2.traverse(t)(CountingVisitor)
      //println(s"assert for time $t: $count1  vs. $count2")
      assert(count1 == count2)
    }
    assert(effect1.traverse(1.s)(CountingVisitor) == 1)
    assert(effect1.traverse(5.s)(CountingVisitor) == 3)
    assert(effect1.traverse(6.s)(CountingVisitor) == 2)
    assert(effect1.traverse(10.s, 10.s)(CountingVisitor) == 3)
    assert(effect1.traverse(step = 15.s)(CountingVisitor) == 4)
  }

  test ( "CountingVisitor works on CompositeEffects alongside another [2pt]") {
    val effect1 = (redFlare + redStar) || (greenFlare + greenClover)
    val effect2 = (redFlare ! greenFlare) + redStar ! greenClover
    val times = List(0.s, 1.s, 5.s, 6.s, 10.s, 11.s)
    for (t <- times) {
      val count1 = effect1.traverse(t)(CountingVisitor)
      val count2 = effect2.traverse(t)(CountingVisitor)
      //println(s"assert for time $t: $count1  vs. $count2")
      assert(count1 == count2)
    }
    assert(effect1.traverse(1.s)(CountingVisitor) == 2)
    assert(effect1.traverse(11.s)(CountingVisitor) == 0)
    assert(effect1.traverse(step = 10.s)(CountingVisitor) == 4)
  }

  /*
  test("Something [3pts]") {
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
    println("Count summary:")
    sequence.evaluateAt[Int]() () (sequence.traverse(_,_)(CountingVisitor))
  }
*/
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
  val countSummary = "{ 0:00.0}\n2\n{ 0:01.0}\n2\n{ 0:02.0}\n2\n{ 0:03.0}\n2\n{ 0:04.0}\n2\n{ 0:05.0}\n3\n{ 0:06.0}\n2\n{ 0:07.0}\n2\n{ 0:08.0}\n2\n{ 0:09.0}\n2\n{ 0:10.0}\n2\n{ 0:11.0}\n1\n{ 0:12.0}\n1\n{ 0:13.0}\n1\n{ 0:14.0}\n1\n{ 0:15.0}\n4\n{ 0:16.0}\n2\n{ 0:17.0}\n2\n{ 0:18.0}\n2\n{ 0:19.0}\n2\n{ 0:20.0}\n2\n"

  test("Larger sequence analyzes to proper volume at key points [4pts]") {
    //using toString for calculated items that may not be exact
    assert(sequence.traverse(0.s)(VolumeVisitor).toString() == 103.db.toString())
    assert(sequence.traverse(9.s)(VolumeVisitor) == 100.db)
    assert(sequence.traverse(15.s)(VolumeVisitor).toString() == 115.3.db.toString())
    assert(sequence.traverse(16.s)(VolumeVisitor) == Double.NegativeInfinity.db)
  }

  test("Larger sequence analyzes to proper count at key points [4pts]") {
    //using toString for calculated items that may not be exact
    assert(sequence.traverse(0.s)(CountingVisitor) == 2)
    assert(sequence.traverse(5.s)(CountingVisitor) == 3)
    assert(sequence.traverse(11.s)(CountingVisitor) == 1)
    assert(sequence.traverse(15.s)(CountingVisitor) == 4)
    assert(sequence.traverse(20.s)(CountingVisitor) == 2)
  }

  def launchOffset(effect: Effect): Seconds = effect match {
    case sve : SkyEffect => 0.s - (sve.breakHeight / 10.0.m).s
    case _ => 0.s
  }

  val some = Flare(red) ! Salute() ! redStar + (greenFlare ! greenClover + (Salute() ! redCombo))
  val lots = Peony(blue) || some || some || some

  test( "sequence.generateTimings() yields appropriate mapping [3pts]") {
    val result = sequence.generateTimings()
    assert(result.size == 3)
    assert(result(0.s).size == 2 && result(5.s).size == 1
      && result(15.s).size == 3)
  }

  test( "lots.generateTimings() yields appropriate mapping [3pts]") {
    val result = lots.generateTimings()
    assert(result.size == 3)
    assert(result(0.s).size == 10 && result(5.s).size == 6
      && result(10.s).size == 9)
  }

  test( "sequence.generateTimings(launchOffset) yields appropriate mapping [3pts]") {
    val result = sequence.generateTimings(launchOffset)
    assert(result.size == 5)
    assert(result(0.s - 1.5.s).size == 1 && result(0.s).size == 1
      && result(3.s).size == 1 && result(12.s).size == 1 && result(13.s).size == 2)
  }

  test( "lots.generateTimings(launchOffset) yields appropriate mapping [3pts]") {
    val result = lots.generateTimings(launchOffset)
    assert(result.size == 5)
    assert(result(0.s - 3.0.s).size == 3 && result(0.s - 2.0.s).size == 7
      && result(3.s).size == 6
      && result(7.s).size == 3 && result(8.s).size == 6)
  }

  test( "MortarSizeVisitor filters in properly [1pt]") {
    val mortars = new MortarSizeVisitor
    assert(mortars.select.isDefinedAt(redStar) == true)
    assert(mortars.select.isDefinedAt(Salute()) == true)
  }

  test( "MortarSizeVisitor filters out properly [1pt]") {
    val mortars = new MortarSizeVisitor
    assert(mortars.select.isDefinedAt(redFlare) == false)
    assert(mortars.select.isDefinedAt(testMusic) == false)
    assert(mortars.select.isDefinedAt(greenCombo) == false)
  }

  test( "MortarSizeVisitor yields appropriate maximum size [2pt]") {
    val mortars = new MortarSizeVisitor
    val largest = sequence.traverse(0.s, sequence.duration)(mortars)
    assert(largest == 0.3.m)
  }

  test( "MortarSizeVisitor yields appropriate counts for sizes [2pt]") {
    val mortars = new MortarSizeVisitor
    sequence.traverse(0.s, sequence.duration)(mortars)
    val listing = mortars.listing
    assert(listing(0.05.m) == 1 &&
           listing(0.2.m) == 1 && listing(0.3.m) == 2)
    val sizes = for ((size -> list) <- listing) yield size
    assert (sizes == List(0.05.m, 0.2.m, 0.3.m))
  }

  test( "MortarSizeVisitor yields appropriate counts for sizes on large nested composite [4pt]") {
    val mortars = new MortarSizeVisitor

    val some = Flare(red) ! Salute() ! redStar + greenFlare ! greenClover + Salute() ! redCombo
    val lots = Peony(blue) || some || some || some
    lots.traverse(0.s, lots.duration)(mortars)
    val listing = mortars.listing
    assert(listing(0.05.m) == 9 && listing(0.2.m) == 6 && listing(0.3.m) == 1)
    val sizes = for ((size -> list) <- listing) yield size
    assert (sizes == List(0.05.m, 0.2.m, 0.3.m))
  }
}