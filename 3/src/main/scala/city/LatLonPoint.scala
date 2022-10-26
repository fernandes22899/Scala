package city

import quadtree.LatLonLocation

/**
  * Builds a location from a latitude and a longitude.
  * Both are expressed in decimal degrees (e.g., `LatLonPoint(48.8583, 2.2945)` is the location
  * of the Eiffel Tower.)
  * Note that two instances of this class are never equal, even if they have the exact
  * same latitude and longitude.
  */
class LatLonPoint(val lat: Double, val lon: Double) extends LatLonLocation[LatLonPoint] {
  def x: Int = (lon * scale).round.toInt
  def y: Int = (lat * scale).round.toInt
  override def toString = f"lat: $lat%.6f, lon: $lon%.6f"
}
