plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":engine"))
}
