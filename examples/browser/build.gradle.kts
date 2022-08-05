plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}
