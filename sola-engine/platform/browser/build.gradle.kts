plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
}

dependencies {
  implementation("org.teavm:teavm-tooling:0.9.0")
  implementation("org.teavm:teavm-classlib:0.9.0")
  implementation("org.teavm:teavm-extras-slf4j:0.9.0")
}

solaPublishing {
  artifactId = "platform-browser"
}
