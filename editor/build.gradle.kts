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
  api(project(":sola-engine:platform:javafx"))

  // Test
  testImplementation("org.mockito:mockito-inline:3.0.0")
  testImplementation("org.mockito:mockito-junit-jupiter:3.0.0")
  testImplementation(platform("org.junit:junit-bom:5.7.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
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
