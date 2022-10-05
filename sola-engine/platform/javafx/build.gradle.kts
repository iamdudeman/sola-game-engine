plugins {
  id("sola.platform-conventions")
  id("org.openjfx.javafxplugin") version "0.0.13"
}

javafx {
  modules("javafx.controls")
  // TODO this might have issues on other platforms (possibly need reliant code in separate module)
  configuration = "api"
}
