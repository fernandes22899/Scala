val UNH_CS_Repository = "UNH/CS repository" at "https://cs.unh.edu/~charpov/lib"
val TinyScalaUtils    = "org.tinyscalautils"     %% "tiny-scala-utils"           % "0.1.2"
val parallel          = "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / crossPaths := false

ThisBuild / scalacOptions ++= List(
  "-deprecation",       // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8", // Specify character encoding used by source files.
  "-explaintypes",      // Explain type errors in more detail.
  "-feature",           // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked",         // Enable detailed unchecked (erasure) warnings.
  "-Xlint",             // Enable recommended additional warnings.
)

ThisBuild / javacOptions ++= List("-deprecation", "-Xlint")

ThisBuild / resolvers += UNH_CS_Repository

lazy val lectures = (project in file(".")).settings(
  Compile / scalaSource := baseDirectory.value / "src" / "scala",
  Compile / javaSource := baseDirectory.value / "src" / "java",
  libraryDependencies ++= List(TinyScalaUtils, parallel)
)
