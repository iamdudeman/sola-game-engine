plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.1.0"
}

dependencies {
  api(project(":sola-engine"))
}

javafx {
  modules("javafx.controls", "javafx.media")
  version = "${project.properties["javaFxVersion"]}"
}

solaPublishing {
  artifactId = "platform-javafx"
}
