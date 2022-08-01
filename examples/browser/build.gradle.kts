plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":sola-engine:platform:browser"))
  api(project(":examples:common"))
}
