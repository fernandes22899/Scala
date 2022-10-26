package cs671.fireworks

/** The base class for all fireworks-related effects.
 *  The primary point of an effect is how it renders
 *  in a larger fireworks display, and so it allows
 *  for the creation of sequences that are timed
 *  according to when the effects appear, rather than
 *  when they are launched.*/
trait Effect {
  def duration: Seconds //Length of effect execution

  def followedBy(other: Effect): CompositeEffect = new CompositeEffect(this).followedBy(other)

  def +(other: Effect): CompositeEffect = this.followedBy(other)
  
  def togetherWith(other: Effect): CompositeEffect = new CompositeEffect(this).togetherWith(other)

  def !(other: Effect): CompositeEffect = this.togetherWith(other)

  def alongside(other: Effect): CompositeEffect = new CompositeEffect(this).alongside(other)

  def ||(other: Effect): CompositeEffect = this.alongside(other)

  def render(timeOffset: Seconds = 0.s, step: Seconds = 1.s): String = {
    val relTime = relativeTime(timeOffset, step)
    //println(s"Rendering $this at $timeOffset, step $step (Relative time $relTime)")
    render(relTime)
  }

  def relativeTime(timeOffset: Seconds = 0.s, step: Seconds = 1.s): Double = {
    if (timeOffset <= 0)
      0.0
    else if (timeOffset > 1)
      (timeOffset + step) / duration
    else
      1.0.min((timeOffset + step) / duration)
  }

  // t represents relative position (0.0 = start, 1.0 = end)
  protected def render(t: Double): String = "\n"
}

object Effect {
  def apply(): Effect = EmptyEffect()
}