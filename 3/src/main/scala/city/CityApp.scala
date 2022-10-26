package city

import quadtree._

/**
  * Simple application to try quadtrees on a collection of US cities.
  */
class CityApp(map: QuadTree[LatLonPoint, City]) {

  /** Nearest city from the given location. */
  def nearestCity(where: LatLonPoint): City = map.getNearest(where)._2

  /** Nearest city from the given location with at least the given population. */
  def nearestCityWithPop(where: LatLonPoint, minPop: Int): City = map.getNearest(where, Some {
    case (_,p) => p.pop >=minPop
  })._2

  /** Nearest city from the given location that is in the given state. */
  def nearestCityInState(where: LatLonPoint, state: String): City = map.getNearest(where, Some {
    case (_,p) => p.state == state
  })._2

  /**
    * All cities within a distance of the given location.
    * The distance is specified in meters.
    */
  def allCitiesWithin(where: LatLonPoint, meters: Int): Iterable[City] = map.getWithin(where, meters).values
}

/**
  * Simple application to try quadtrees on a collection of US cities.
  */
object CityApp {

  /** Loads the cities from resources as a regular map. */
  private def loadCities(): Map[LatLonPoint, City] = {
    val url = getClass.getClassLoader.getResource("us-cities.txt")
    val cities = City.fromURL(url)
    val b = Map.newBuilder[LatLonPoint, City]
    for (city <- cities) {
      val loc = new LatLonPoint(city.latitude, city.longitude)
      b += (loc -> city)
    }
    b.result()
  }

  /** Command-line application. */
  def main(args: Array[String]): Unit = {

    val threshold = args(0).toInt
    val map = QuadTree(threshold, loadCities())
    println(s"map constructed (${map.size} elements)")

    val here = new LatLonPoint(43.134101, -70.934914) // Kingsbury Hall
    val eiffel = new LatLonPoint(48.8583, 2.2945) // Eiffel Tower

    val app = new CityApp(map)

    // closest US city from here
    println(app.nearestCity(here))

    // closest US city from the Eiffel Tower
    println(app.nearestCity(eiffel))

    // closest US city from here with a population of at least 15000
    println(app.nearestCityWithPop(here, 15000))

    // closest MA city from here
    println(app.nearestCityInState(here, "MA"))

    // all cities with 10km from here
    println((for (c <- app.allCitiesWithin(here, 10000)) yield c.name).toList.sorted)
  }
}
