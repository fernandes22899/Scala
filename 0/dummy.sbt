lazy val dummy = (project in file(".")).settings(
  name := "dummy",
  version := "1.2.3",
  Test / javaOptions += "-Xmx1G"
)
