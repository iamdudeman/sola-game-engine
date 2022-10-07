plugins {
  id("sola.platform-conventions")
}

dependencies {
  // Note: TeaVM:2b671b8088 contains needed features [records, Random#nextInt(origin, bounds), Random#nextFloat(origin, bounds), Thread#setName]
  implementation("com.github.konsoletyper.teavm:teavm-classlib:2b671b8088")
  implementation("com.github.konsoletyper.teavm:teavm-tooling:2b671b8088")
  implementation("org.teavm:teavm-extras-slf4j:0.7.0")
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
