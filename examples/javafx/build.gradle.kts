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
  implementation(project(":platform:javafx"))
  implementation(project(":examples:common"))
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.javafx.JavaFxMain"
  }
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}

tasks.withType<Zip>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
