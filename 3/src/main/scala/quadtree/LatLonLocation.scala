package quadtree

/**
  * A location based on latitude and longitude on Earth.
  * These locations belong to a non-Euclidean geometry and distances are defined in terms of great
  * circles.  For simplicity, the Earth is assumed to be a perfect sphere of radius 6371km.
  *
  * The code used in this trait is based on Java code written by Jan Philip Matuschek.
  * See [[[http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates]]] for details.
  */
trait LatLonLocation[Loc <: Location[Loc]] extends Location[Loc] {

  /**
    * The scale used to represent latitudes and longitudes using integers.
    * By default, it is 1e6, which means that `x` and `y` are expressed in millionth of degrees.
    */
  protected val scale: Double = 1e6 // units are millionth of degree

  import math.{ Pi, sin, cos, asin, acos }

  private def fromInt(n: Int) = (n / scale).toRadians
  private def toInt(x: Double) = (x.toDegrees * scale).round.toInt

  private def lat(loc: Location[_]): Double = fromInt(loc.y)
  private def lon(loc: Location[_]): Double = fromInt(loc.x)

  /** Distance between this and that location, based on great circles. */
  def distanceTo(that: Loc): Double = {
    acos(
      sin(lat(this)) * sin(lat(that)) +
        cos(lat(this)) * cos(lat(that)) * cos(lon(this) - lon(that))
    ) * 6371000.0
  }

  /** Surrounding space, based on great circles.  The distance is specified in meters. */
  def around(meters: Double): Space = {
    import LatLonLocation.{ halfPi, doublePi }

    val a = meters / 6371000.0
    val lat = this.lat(this)
    val lon = this.lon(this)
    var minLat = lat - a
    var maxLat = lat + a
    var minLon = 0.0
    var maxLon = 0.0
    if (minLat > -halfPi && maxLat < halfPi) {
      val dlon = asin(sin(a) / cos(lat))
      minLon = lon - dlon
      if (minLon < -Pi) minLon += doublePi
      maxLon = lon + dlon
      if (maxLon > Pi) maxLon -= doublePi
    }
    else { // a pole within the distance
      minLat = minLat max -halfPi
      maxLat = maxLat min halfPi
      minLon = -Pi
      maxLon = Pi
    }
    Space(toInt(minLon), toInt(minLat), toInt(maxLon), toInt(maxLat))
  }
}

/**
  * Companion object of the `LatLonLocation` trait.
  */
private object LatLonLocation {
  private final val halfPi = math.Pi / 2.0
  private final val doublePi = math.Pi * 2.0
}
