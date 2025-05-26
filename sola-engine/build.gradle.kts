dependencies {
  api("com.github.iamdudeman:sola-ecs:2.2.0")
  api("com.github.iamdudeman:sola-json:4.0.1")
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
