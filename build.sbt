name := "hoy-como-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.auth0" % "java-jwt" % "3.3.0",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

resolvers := Seq("typesafe" at "http://repo.typesafe.com/typesafe/releases/")

play.Project.playJavaSettings
