plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.sola-publishing")
}

val teaVmVersion = "0.10.0"

dependencies {
  api(project(":sola-engine"))

  implementation("org.teavm:teavm-tooling:$teaVmVersion")
  implementation("org.teavm:teavm-classlib:$teaVmVersion")
}

solaPublishing {
  artifactId = "platform-browser"
}
