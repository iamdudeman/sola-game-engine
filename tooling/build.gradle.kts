plugins {
  id("sola.java-conventions")
  id("technology.sola.sola-java-distribution")
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
