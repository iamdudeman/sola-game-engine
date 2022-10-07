dependencies {
  // todo switch to 2.0.6 when it is released
  api("com.github.iamdudeman:sola-ecs:2301f68106")
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
