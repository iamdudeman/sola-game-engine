dependencies {
  // todo replace with version instead of commit hash when it is published
  api("com.github.iamdudeman:sola-ecs:72b900f020")
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
