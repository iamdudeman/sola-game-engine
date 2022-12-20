plugins {
  id("sola.platform-conventions")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "platform-server"

      from(components["java"])
    }
  }
}
