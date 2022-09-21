plugins {
  id("sola.java-conventions")
  id("application")
}

dependencies {
  implementation(project(":sola-engine"))
}

application {
  mainClass.set("technology.sola.engine.tooling.ToolingMain")
}
