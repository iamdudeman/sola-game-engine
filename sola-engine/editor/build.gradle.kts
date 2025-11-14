plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.sola-publishing")
  id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
  modules("javafx.controls")
  version = "${project.properties["javaFxVersion"]}"
}

dependencies {
  api(project(":sola-engine"))

  implementation(project(":sola-engine:platform:javafx"))
  implementation(project(":tooling"))
}

solaPublishing {
  artifactId = "editor"
}
