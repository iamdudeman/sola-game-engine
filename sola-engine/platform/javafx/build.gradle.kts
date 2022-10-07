plugins {
  id("sola.platform-conventions")
  id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
  modules("javafx.controls")
  // TODO this might have issues on other platforms (possibly need reliant code in separate module)
  configuration = "api"
  version = "17"
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
