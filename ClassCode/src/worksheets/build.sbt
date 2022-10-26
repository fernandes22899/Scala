name := "Worksheets"

version := "1.0"

val UNH_CS_Repository = "UNH/CS repository" at "https://cs.unh.edu/~charpov/lib"
val UNH_CS            = "edu.unh.cs" % "classutils" % "1.6.3" % Test
val TinyScalaUtils    = "org.tinyscalautils" %% "tiny-scala-utils" % "0.2.0"
val parallel          = "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"

ThisBuild / scalaVersion := "2.13.6"

ThisBuild / Test / fork := true
ThisBuild / Test / parallelExecution := false
ThisBuild / Test / run / outputStrategy := Some(StdoutOutput)

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",       // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8", // Specify character encoding used by source files.
  "-explaintypes",      // Explain type errors in more detail.
  "-feature",           // Emit warning for usages of features that should be imported explicitly.
  "-unchecked",         // Enable detailed unchecked (erasure) warnings.
  "-Xlint",             // Enable recommended additional warnings.
)

ThisBuild / resolvers += UNH_CS_Repository
ThisBuild / libraryDependencies ++= List(UNH_CS, TinyScalaUtils, parallel)
