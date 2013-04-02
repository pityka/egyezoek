import sbt._
import sbt.Keys._

object EgyezesekBuild extends Build {

  val scalatest = "org.scalatest" % "scalatest" % "1.9.1" % "test" cross CrossVersion.binaryMapped {
  case "2.9.0" => "2.9.0" // remember that pre-2.10, binary=full
  case "2.9.1" => "2.9.0" // remember that pre-2.10, binary=full
  case "2.9.2" => "2.9.0" // remember that pre-2.10, binary=full
  case "2.10" => "2.10" // useful if a%b was released with the old style
  case "2.10.0" => "2.10"
  case "2.10.1" => "2.10"
}

  lazy val proj = Project(
    id = "egyezesek",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "egyezesekkeresese",
      organization := "pityu",
      version := "1.0",
      scalaVersion := "2.10.1",
      scalacOptions += "-deprecation",
      scalacOptions += "-feature",

      scalacOptions += "-unchecked",
      // add other settings here
      libraryDependencies ++= Seq(scalatest)
      
    )
  )
}
