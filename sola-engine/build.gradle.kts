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

val fatJar = task("fatJar", type = Jar::class) {
  archiveBaseName.set("${project.name}-fat")
  group = "build"
  manifest {
    attributes["Implementation-Title"] = "Sola Engine"
    attributes["Implementation-Version"] = archiveVersion
  }
  from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
  with(tasks.jar.get() as CopySpec)
}

tasks {
  "build" {
    dependsOn(fatJar)
  }
}
