package cs671.fireworks

/** Empty effect of zero duration, useful for certain aggregate functions */
case class EmptyEffect() extends Effect {
  val duration = 0.s

  override protected def render(t: Double): String = {
    //println("EmptyEffect Render")
    ""
  }
}

case class Music(name: String, duration: Seconds, volume: Decibels, file: String, lyrics: String) extends SoundEffect {
  override def baseSound = lyrics

  override def continueSound: String = s">$name continues playing< [@ $volume] "+ super.continueSound
}

trait SkyVisualEffect extends SkyEffect with VisualEffect

trait PopEffect extends SoundEffect {
  def volume = 100.db
}





