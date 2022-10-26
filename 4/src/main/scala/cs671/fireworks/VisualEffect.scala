package cs671.fireworks

import cs671.fireworks.Color.Color

/** A visual effect on the ground or in the air--something
 * with a particular height, size, color, etc. that is
 * rendered to text as well.
 */
trait VisualEffect extends Effect {
  def spreadDiameter: Meters

  def spreadVertical: Meters

  def color: Color

  def altColor: Option[Color] = None

  def hasTails: Boolean = false

  def shape = "bloom"

  def baseVisual = "star"

  def isSingular = false

  override def render(timeOffset: Seconds = 0.s, step: Seconds = 1.s): String = {
    val relTime = relativeTime(timeOffset, step)
    //println(s"Rendering $this at $timeOffset, step $step (Relative time $relTime)")
    render(relTime) + (if (relTime <= 0 && timeOffset + step >= duration) render(1.0) else "")
  }

  override def render(t: Double): String = ???
    /*t match {
  case a if a > 1 => "" + super.render(t)
    case 0.0 => f"<$color flash> " + super.render(t)
    case _ => (isSingular match {
      case true => f"<$color $shape $baseVisual> "
      case false => f"<$color $shape of ${baseVisual}s [${spreadDiameter * t} x ${spreadVertical * t}]> "
    }) + super.render(t)
  }



     */

}
