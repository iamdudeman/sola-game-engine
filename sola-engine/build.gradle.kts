dependencies {
  api("com.github.iamdudeman:sola-ecs:2.1.5")
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
