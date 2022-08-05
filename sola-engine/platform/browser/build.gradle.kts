plugins {
  id("sola.platform-conventions")
}

dependencies {
  /*
  TODO replace with future TeaVM version from maven that supports required features
   * records,
   * Random#nextInt(origin, bounds)
   * Random#nextFloat(origin, bounds)
   * Thread#setName
   */
  implementation(files("libs/teavm-classlib-0.7.0-SNAPSHOT.jar"))
  implementation(files("libs/teavm-core-0.7.0-SNAPSHOT.jar"))

  implementation("org.teavm:teavm-jso-apis:0.7.0")

  // Build script
  implementation("org.teavm:teavm-tooling:0.7.0")
  implementation("org.teavm:teavm-classlib:0.7.0")

  // Logging
  implementation("org.teavm:teavm-extras-slf4j:0.7.0")
}
