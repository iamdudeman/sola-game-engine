dependencies {
  api("com.github.iamdudeman:sola-ecs:2.1.0")
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
