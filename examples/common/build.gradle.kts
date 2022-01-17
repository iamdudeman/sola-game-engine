plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine"))
}
