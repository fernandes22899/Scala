package cs671.fireworks

/** An audible effect, such as an explosion, whistles, or music.
 * SoundEffects have a particular volume and rendering of their
 * sound.
 */
trait SoundEffect extends Effect {
  def volume: Decibels

  def baseSound = "Pop"
  def continueSound = ""
  def hasContinualSound = !continueSound.isEmpty()

  override def render(t: Double): String = {
    if(t > 1.0) ""
    else {
      (if(t == 0) s">$baseSound< [@ ${volume}]"
      else continueSound) + super.render(t)
    }
  }
}
