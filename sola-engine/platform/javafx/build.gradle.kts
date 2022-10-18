plugins {
  id("sola.platform-conventions")
  id("org.openjfx.javafxplugin") version "0.0.13"
}

javafx {
  modules("javafx.controls")
  version = "17.0.2"
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "platform-javafx"

      from(components["java"])
    }
  }
}
