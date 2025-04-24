plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
}

val teaVmVersion = "0.10.0"

dependencies {
  implementation("org.teavm:teavm-tooling:$teaVmVersion")
  implementation("org.teavm:teavm-classlib:$teaVmVersion")
}

solaPublishing {
  artifactId = "platform-browser"
}
