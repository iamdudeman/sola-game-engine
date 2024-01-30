plugins {
  id("sola.java-conventions")
  id("technology.sola.sola-java-distribution")
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

solaJavaDist {
  mainClass = "technology.sola.engine.tooling.ToolingMain"
}
