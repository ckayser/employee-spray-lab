name := "spray-lab"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
    "io.spray"              %   "spray-routing"     % "1.1-M7",
    "io.spray"              %   "spray-can"         % "1.1-M7",
    "com.typesafe.akka"     %%  "akka-actor"        % "2.1.2",
    "com.typesafe.akka"     %%  "akka-kernel"       % "2.1.2",
    "io.spray"              %%  "spray-json"        % "1.2.3",
    "io.spray"              %   "spray-testkit"     % "1.1-M7"      % "test",
    "org.specs2"            %%  "specs2"            % "1.14"        % "test",
    "com.typesafe.akka"     %%  "akka-testkit"      % "2.1.2"       % "test"
)

resolvers ++= Seq(
    "Spray repo" at "http://repo.spray.io",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype releases"  at "http://oss.sonatype.org/content/repositories/releases"
)