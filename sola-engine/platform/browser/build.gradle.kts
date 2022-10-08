plugins {
  id("sola.platform-conventions")
}

// Note: TeaVM:2b671b8088 contains needed features [records, Random#nextInt(origin, bounds), Random#nextFloat(origin, bounds), Thread#setName]
val teavmVersion = "2b671b8088"

dependencies {
  implementation("com.github.konsoletyper.teavm:teavm-classlib:${teavmVersion}")
  implementation("com.github.konsoletyper.teavm:teavm-tooling:${teavmVersion}")
  implementation("com.github.konsoletyper.teavm:teavm-extras-slf4j:${teavmVersion}")
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
