ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.3"

lazy val root = (project in file("."))
  .settings(
    name := "distributedSort",

    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-netty" % "1.58.0",
      "io.grpc" % "grpc-protobuf" % "1.58.0",
      "io.grpc" % "grpc-stub" % "1.58.0",
      "com.google.protobuf" % "protobuf-java" % "4.33.1",
      "javax.annotation" % "javax.annotation-api" % "1.3.2"
    )
  )
