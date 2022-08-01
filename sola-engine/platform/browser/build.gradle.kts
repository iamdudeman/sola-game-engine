plugins {
  id("sola.platform-conventions")
}

dependencies {
  implementation("org.teavm:teavm-jso-apis:0.7.0")

  // Build script
  implementation("org.teavm:teavm-tooling:0.7.0")
  implementation("org.teavm:teavm-classlib:0.7.0")

  // Logging
  implementation("org.teavm:teavm-extras-slf4j:0.7.0")
}
