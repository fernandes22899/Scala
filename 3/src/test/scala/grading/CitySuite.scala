package grading

import city.LatLonPoint

class CitySuite extends SampleCityTests with GradingSuite {

  val mgm = new LatLonPoint(36.102576, -115.172442) // MGM Grand

  test("nearest city (2)") {
    val city = app.nearestCity(mgm)
    assert(city.name === "Paradise")
  }

  test("nearest city with pop (2)") {
    val city = app.nearestCityWithPop(mgm, 1000000)
    assert(city.name === "Los Angeles")
  }

  test("nearest city in state (2)") {
    val city = app.nearestCityInState(mgm, "AZ")
    assert(city.name === "Dolan Springs")
  }

  test("all cities within (2)") {
    assert(app.allCitiesWithin(mgm, 10000).toList.map(_.name).sorted ===
      List("Las Vegas", "Paradise", "Spring Valley", "Winchester")
    )
  }
}
