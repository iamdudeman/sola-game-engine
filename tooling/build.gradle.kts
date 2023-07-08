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

tasks.withType<Zip> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task("distFatJar", Jar::class) {
  group = "distribution"
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  archiveBaseName.set("sola-${project.name}")

  manifest {
    attributes["Main-Class"] = "technology.sola.engine.tooling.ToolingMain"
  }

  val dependencies = configurations.runtimeClasspath.get().map(::zipTree)

  from(dependencies)
  with(tasks.jar.get())
  dependsOn(configurations.runtimeClasspath)
}
