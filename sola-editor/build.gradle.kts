plugins {
  id("application")
  id("sola.java-conventions")
}

application {
  mainClass.set("technology.sola.engine.editor.EditorMain")
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
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

  archiveBaseName.set("sola-engine-${project.name}")
}
