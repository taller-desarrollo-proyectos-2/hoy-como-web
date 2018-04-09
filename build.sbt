name := "hoy-como-play"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.10.2", "2.10.3", "2.11.8")

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

play.Project.playJavaSettings
