plugins {
  id("application")
}

application {
  mainClass.set("technology.sola.engine.examples.swing.SwingMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:swing"))
  implementation(project(":examples:common"))
}

tasks.withType<Zip>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
