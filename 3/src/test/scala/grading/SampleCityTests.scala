package grading

import city.LatLonPoint
import edu.unh.cs.mc.utils.Resources
import org.scalatest.funsuite.AnyFunSuite

class SampleCityTests extends AnyFunSuite with Resources {

  import city.{ City, CityApp }
  import quadtree._

  lazy val map = {
    val cities = City.fromURL(getResource("us-cities.txt"))
    val b = Map.newBuilder[LatLonPoint, City]
    for (city <- cities) {
      val loc = new LatLonPoint(city.latitude, city.longitude)
      b += (loc -> city)
    }
    QuadTree(64, b.result())
  }

  lazy val app = new CityApp(map)

  val mountRushmore = new LatLonPoint(43.879037, -103.459088) // Mount Rushmore

  test("space") {
    assert(map.space === Space(-166542206, 19068609, -66989983, 71290581))
  }

  test("nearest city (1)") {
    val city = app.nearestCity(mountRushmore)
    assert(city.name === "Custer")
  }

  test("nearest city with pop (1)") {
    val city = app.nearestCityWithPop(mountRushmore, 10000)
    assert(city.name === "Rapid City")
  }

  test("nearest city in state (1)") {
    val city = app.nearestCityInState(mountRushmore, "WY")
    assert(city.name === "Newcastle")
  }

  test("all cities within (1)") {
    assert(app.allCitiesWithin(mountRushmore, 50000).toList.map(_.name).sorted ===
      List(
        "Blackhawk", "Box Elder", "Colonial Pine Hills", "Custer",
        "Hot Springs", "Rapid City", "Rapid Valley", "Summerset"
      ))
  }
}
