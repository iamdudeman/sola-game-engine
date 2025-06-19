plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.0.14"
}

dependencies {
  api(project(":sola-engine"))
}

javafx {
  modules("javafx.controls")
  version = "${project.properties["javaFxVersion"]}"
}

solaPublishing {
  artifactId = "platform-javafx"
}
