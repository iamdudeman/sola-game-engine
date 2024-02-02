plugins {
  id("sola.platform-conventions")
  id("technology.sola.sola-publishing")
}

dependencies {
  implementation("org.teavm:teavm-tooling:0.9.2")
  implementation("org.teavm:teavm-classlib:0.9.2")
  implementation("org.teavm:teavm-extras-slf4j:0.9.2")
}

solaPublishing {
  artifactId = "platform-browser"
}
