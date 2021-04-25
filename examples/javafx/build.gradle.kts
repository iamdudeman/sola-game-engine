plugins {
  id("application")
  id("java-library")
}

application {
  mainClass.set("technology.sola.engine.examples.Main")
}

repositories {
  mavenCentral()
}

dependencies {
//  implementation(project(":engine"))
  api(project(":platform:javafx"))
}

tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.Main"
  }

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
