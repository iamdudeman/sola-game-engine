plugins {
  id("sola.platform-conventions")
}

dependencies {
  implementation("org.teavm:teavm-tooling:0.9.0")
  implementation("org.teavm:teavm-classlib:0.9.0")
  implementation("org.teavm:teavm-extras-slf4j:0.9.0")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "platform-browser"

      from(components["java"])
    }
  }
}
