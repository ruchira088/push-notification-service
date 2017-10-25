name := """push-notification-service"""
organization := "com.ruchij"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" % "play-slick_2.12" % "3.0.2"
libraryDependencies += "com.typesafe.play" % "play-slick-evolutions_2.12" % "3.0.2"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ruchij.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ruchij.binders._"
