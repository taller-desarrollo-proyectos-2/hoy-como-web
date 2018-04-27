name := "hoy-como-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.auth0" % "java-jwt" % "3.3.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.google.maps" % "google-maps-services" % "0.2.7",
  "org.slf4j" % "slf4j-nop" % "1.7.25"
)

resolvers := Seq("typesafe" at "http://repo.typesafe.com/typesafe/releases/")

play.Project.playJavaSettings