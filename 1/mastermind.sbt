lazy val mastermind = (project in file(".")).settings(
  libraryDependencies += "specs" % "practice-specs" % "1.3",
  Test / javaOptions += "-Xmx8G"
)
