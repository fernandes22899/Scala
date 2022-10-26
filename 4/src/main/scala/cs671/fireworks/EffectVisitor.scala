package cs671.fireworks

trait EffectVisitor[A, E <: Effect] {

  def select: PartialFunction[Effect, E] = {case e: E => e}
  def apply(effect: E, t: Seconds, step: Seconds): A

  def identity: A
  def combine(left: A, right: A): A
}

trait StringAdder {
  def identity = ""
  def combine(left: String, right: String): String = f"$left$right"
}

trait DecibelAdder {
  def identity = Double.NegativeInfinity.db
  def combine(left: Decibels, right: Decibels): Decibels = left + right
}

object RenderVisitor extends EffectVisitor[String, Effect] with StringAdder {
  def apply(effect: Effect, t: Seconds, step: Seconds): String = {
    effect.render(t, step)
  }
}

object VolumeVisitor extends EffectVisitor[Decibels, SoundEffect] with DecibelAdder {
  override def select: PartialFunction[Effect, SoundEffect] = {case s: SoundEffect => s}
  def apply(effect: SoundEffect, t: Seconds, step: Seconds): Decibels = {
    if(!effect.hasContinualSound && t > 0)
      Double.NegativeInfinity.db
    else
      effect.volume
  }
  //

}

class MortarSizeVisitor extends EffectVisitor[Meters, Effect with SkyEffect] {
  private var tb: Map[Meters,Integer] = Map()
  override def select: PartialFunction[Effect, Effect with SkyEffect] = {
    case s: Effect with SkyEffect if s.needsMortar => s
  }
  def apply(effect: Effect with SkyEffect, t: Seconds, step: Seconds): Meters = {
    listing.get(effect.tubeSize) match {
      case Some(e) => {
        tb = tb.updated(effect.tubeSize, e + 1)
      }
      case None => {
        def no(a: Any): Int = a.toString.toInt
        tb = (tb + (effect.tubeSize -> 1)).toSeq.sortBy(_._1.meters).toMap.map({case(start,finish) => (start,no(finish))})
      }
    }
    effect.tubeSize
  }
  def identity: Meters = 0.m
  def combine(left: Meters, right: Meters): Meters = ???

  def listing: Map[Meters, Integer] = tb
}