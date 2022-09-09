plugins {
  id("sola.platform-conventions")
}

val fatJar = task("fatJar", type = Jar::class) {
  archiveBaseName.set("sola-engine-${project.name}-fat")
  group = "build"
  manifest {
    attributes["Implementation-Title"] = "Sola Engine Platform ${project.name}"
    attributes["Implementation-Version"] = archiveVersion
  }
  from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
  with(tasks.jar.get() as CopySpec)
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
  "build" {
    dependsOn(fatJar)
  }
}
