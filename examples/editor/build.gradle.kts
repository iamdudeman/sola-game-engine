plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.plugins.sola-javafx")
}

dependencies {
  implementation(project(":sola-engine:editor"))
  implementation(project(":examples:common"))
}
