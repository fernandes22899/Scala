package cs671.fireworks

/** An effect that is launched into the sky */
trait SkyEffect { self: Effect =>
  def breakHeight: Meters

  def tubeSize: Meters

  def needsMortar: Boolean

  def weight: Double
}
