package cs671.fireworks

// We care about effects for timing purposes:
// * Sound (Explosions, Whistles, Sparkles; Music)
// * Light

// Sky can be self-propelled (SkyRocket) or shot into air by mortal (AerialShell)

// Specific effects
// Peony -- spherical break of colored stars that burn without a tail effect
// Ring -- stars arranged to create a ring, smiley face, heart, and clover, etc.
// Chrysanthemum(/spider?) -- like Peony but with trails of sparks
// Crossette -- split into 4, split into 4 again
// Fish -- propelled/swimming stars
// Horsetail/waterfall -- long-burning tail stars that are not propelled far--use gravity
// Multi-break/Bouquet shell -- container for multiple
// Palm -- Trail going up to break, trails out from break (maybe some coconuts at end of tendrils)
// Salute -- Just the large flash/bang
// Comet -- Shimmering trail on the way up.
// Time Rain -- Slow-burning stars (last a long time)
// Willow
// Pistil
// Dragon's Eggs

// Noises: Bang, crackle, hummer (whiz/hum), whistle

// Roman Candle -- fires large stars at regular intervals, sometimes arranged in criss-crossing or fan shapes
// Fountains -- streams of sparks on the ground
// Grids -- like flags, etc.
// Could be mounted on buildings, etc.

import Color._

case class Flare(color: Color, breakHeight: Meters = 20.m) extends SkyVisualEffect {
  val duration = 5.s;
  val spreadDiameter = 0.3.m
  override val shape = "constant"
  override val isSingular = true
  def spreadVertical = spreadDiameter
  def tubeSize = 0.1.m
  def needsMortar = false
  def weight = 0.5
}

case class Peony(color: Color, spreadDiameter: Meters = 10.m,
                 breakHeight: Meters = 20.m) extends SkyVisualEffect with PopEffect {
  val duration = 5.s;
  def spreadVertical = spreadDiameter
  def tubeSize = spreadDiameter * 0.03
  def needsMortar = true
  def weight = spreadDiameter.meters * 0.3
}

case class Ring(override val shape: String, color: Color,
                spreadDiameter: Meters = 10.m,
                breakHeight: Meters = 20.m)  extends SkyVisualEffect with PopEffect {
  val duration = 5.s;
  def spreadVertical = 2.m
  def tubeSize = spreadDiameter * 0.01
  def needsMortar = true
  def weight = spreadDiameter.meters * 0.1
}

case class Salute() extends SkyEffect with SoundEffect {
  val duration = 0.2.s
  val breakHeight = 30.m
  override val baseSound = "BOOM"
  override val volume = 115.db
  def tubeSize = 0.2.m
  def needsMortar = true
  def weight = 0.2
}

// We want sequences to be declarative, where each
// effect follows the other (or happens simultaneously)
//   ! indicates simultaneous (0 second delay)
//   ! 5 indicates offset of 5 seconds after prior effect started
//   + indicates follows immediately after prior effect stopped
//   + 5 indicates offset of 5 seconds after prior effect stopped
//
