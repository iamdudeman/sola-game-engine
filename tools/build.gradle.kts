plugins {
  id("application")
  id("java-library")
}

application {
  mainClass.set("technology.sola.engine.tools.ToolsMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.google.code.gson:gson:2.8.7")
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.tools.ToolsMain"
  }

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
