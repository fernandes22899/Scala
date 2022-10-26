package city

import java.net.URL

/**
  * Data associated with a city.
  *
  * Latitude and longitude are in decimal degrees.  Elevation is optional (but can be negative).
  * State has to be the usual 2-letter string (50 states + DC)
  */
case class City(name: String, state: String, pop: Int, elev: Option[Int],
                latitude: Float, longitude: Float) {
  require(pop >= 0, s"negative population: $pop")
  require(latitude >= -90 && latitude <= 90)
  require(longitude >= -180 && longitude <= 180)
  require(City.states(state), s"unknown state abbreviation: $state")
}

/**
  * Companion object of the [[City]] class.
  */
object City {

  private val states =
    Set(
      "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
      "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
      "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
      "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
      "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY",
      "DC"
    )

  /**
    * Loads a list of cities from a source.  Each line of the source is of the form:
    * `"name ; state ; latitude ; longitude ; population ; elevation"`
    */
  def fromURL(url: URL): Seq[City] = {
    import scala.collection.immutable.ArraySeq
    import scala.io.Source
    import scala.util.Using

    Using.resource(Source.fromURL(url)) { source =>
      val load = for (line <- source.getLines() if line.trim.nonEmpty) yield {
        val a = line.split(';')
        val name = a(0).trim
        val state = a(1).trim
        val latitude = a(2).toFloat
        val longitude = a(3).toFloat
        val pop = a(4).trim.toInt
        val elev = a(5).trim.toIntOption
        City(name, state, pop, elev, latitude, longitude)
      }
      load.to(ArraySeq)
    }
  }
}
