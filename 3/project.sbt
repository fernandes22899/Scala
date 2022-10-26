lazy val quadtree = (project in file("."))
  .settings(
    Test / javaOptions += "-Xmx1G"
  )
