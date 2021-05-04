plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":engine"))
}

tasks.withType<Jar>() {
  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
