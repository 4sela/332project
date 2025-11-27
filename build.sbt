ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.3"

lazy val root = (project in file("."))
  .settings(
    name := "distributedSort",

    // Just the basic dependencies - no complicated stuff!
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-netty" % "1.58.0",
      "io.grpc" % "grpc-protobuf" % "1.58.0",
      "io.grpc" % "grpc-stub" % "1.58.0",
      "com.google.protobuf" % "protobuf-java" % "3.24.0",
      "javax.annotation" % "javax.annotation-api" % "1.3.2"
    )
  )