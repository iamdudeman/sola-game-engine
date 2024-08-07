plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.0.14"
}

javafx {
  modules("javafx.controls")
  version = "${project.properties["javaFxVersion"]}"
}

solaPublishing {
  artifactId = "platform-javafx"
}
