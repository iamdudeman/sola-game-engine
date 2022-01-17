plugins {
  id("java-library")
}

tasks.jar {
  archiveBaseName.set("sola-engine-examples-${project.name}")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine"))
}
