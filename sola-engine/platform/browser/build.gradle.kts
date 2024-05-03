plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
}

val teaVmVersion = "0.10.0"

dependencies {
  implementation("org.teavm:teavm-tooling:$teaVmVersion")
  implementation("org.teavm:teavm-classlib:$teaVmVersion")
  implementation("org.teavm:teavm-extras-slf4j:$teaVmVersion")
}

solaPublishing {
  artifactId = "platform-browser"
}
