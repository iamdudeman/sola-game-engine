plugins {
  id("application")
  id("sola.java-conventions")
}

application {
  mainClass.set("technology.sola.engine.examples.javafx.JavaFxMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
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

  archiveBaseName.set("sola-engine-examples-${project.name}")
}

tasks.withType<Zip>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
