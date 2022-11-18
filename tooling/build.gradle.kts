plugins {
  id("sola.java-conventions")
}

dependencies {
  implementation(project(":sola-engine"))
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "tooling"

      from(components["java"])
    }
  }
}
