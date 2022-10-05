plugins {
  id("sola.java-conventions")
  id("maven-publish")
}

dependencies {
  api(project(":sola-engine"))
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "platform-${project.name}"

      from(components["java"])
    }
  }
}
