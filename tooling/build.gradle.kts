plugins {
  id("application")
}

dependencies {
  implementation(project(":sola-engine"))
}

application {
  mainClass.set("technology.sola.engine.tooling.ToolingMain")
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
