plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.plugins.sola-java-distribution")
  id("technology.sola.sola-publishing")
}

dependencies {
  implementation(project(":sola-engine"))
}

solaJavaDist {
  mainClass = "technology.sola.engine.tooling.ToolingMain"
}

solaPublishing {
  artifactId = "tooling"
}
