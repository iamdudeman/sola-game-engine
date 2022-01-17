plugins {
  id("sola.platform-conventions")
  id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
  modules("javafx.controls", "javafx.fxml")
  configuration = "api" // TODO this might have issues on other platforms (possibly need reliant code in separate module)
}
