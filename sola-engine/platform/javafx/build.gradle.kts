plugins {
  id("sola.platform-conventions")
  id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
  modules("javafx.controls", "javafx.fxml")
  configuration = "api"
}
