name := "scala-unit-tests"
version := "1.0.0"
scalaVersion := "2.13.12"
crossScalaVersions := Seq("2.13.12", "3.3.3", "3.6.0")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)

// Para correr un test especifico: sbt "testOnly *P01*"
// Para todos: sbt test
