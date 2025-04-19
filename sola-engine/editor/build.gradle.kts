plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.0.14"
}

javafx {
  modules("javafx.controls")
  version = "${project.properties["javaFxVersion"]}"
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
  implementation(project(":tooling"))
}

solaPublishing {
  artifactId = "editor"
}
