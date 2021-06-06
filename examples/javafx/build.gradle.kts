plugins {
  id("application")
  id("java-library")
}

application {
  mainClass.set("technology.sola.engine.examples.javafx.JavaFxMain")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":platform:javafx"))
  api(project(":examples:common"))
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.javafx.JavaFxMain"
  }

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
