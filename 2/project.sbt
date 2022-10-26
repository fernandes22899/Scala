lazy val polynomials = (project in file("."))
  .settings(
    Test / javaOptions += "-Xmx1G"
  )
