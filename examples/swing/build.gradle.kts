plugins {
  id("application")
}

application {
  mainClass.set("technology.sola.engine.examples.swing.SwingMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:swing"))
  implementation(project(":examples:common"))
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.swing.SwingMain"
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
