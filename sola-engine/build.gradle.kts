plugins {
  id("sola.java-conventions")
  id("maven-publish")
}

dependencies {
  api("com.github.iamdudeman:sola-ecs:2.0.5")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "sola-engine"

      from(components["java"])
    }
  }
}
