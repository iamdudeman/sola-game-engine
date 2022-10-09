plugins {
  id("application")
  id("org.openjfx.javafxplugin") version "0.0.13"
}

application {
  mainClass.set("technology.sola.engine.examples.javafx.JavaFxMain")
}

javafx {
  modules("javafx.controls")
  version = "17.0.2"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
  implementation(project(":examples:common"))
}

tasks.withType<Zip>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
