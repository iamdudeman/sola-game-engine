plugins {
  id("application")
  id("java-library")
}

application {
  mainClass.set("technology.sola.engine.editor.Main")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":platform:javafx"))
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.editor.Main"
  }
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
