plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.sola-publishing")
}

dependencies {
  api(project(":sola-engine"))
}

solaPublishing {
  artifactId = "platform-swing"
}
