plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.0.13"
}

javafx {
  modules("javafx.controls")
  version = "17.0.2"
}

solaPublishing {
  artifactId = "platform-javafx"
}
