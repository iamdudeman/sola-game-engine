plugins {
  id("application")
}

application {
  mainClass.set("technology.sola.engine.examples.javafx.JavaFxMain")
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
