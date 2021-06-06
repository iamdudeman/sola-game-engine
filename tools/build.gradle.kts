plugins {
  id("application")
  id("java-library")
}

application {
  mainClass.set("technology.sola.engine.tools.Main")
}

repositories {
  mavenCentral()
}

dependencies {

}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.tools.Main"
  }

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
