plugins {
  id("sola.platform-conventions")
}

dependencies {
  implementation("org.teavm:teavm-tooling:0.8.1")
  implementation("org.teavm:teavm-classlib:0.8.1")
  implementation("org.teavm:teavm-extras-slf4j:0.8.1")
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
