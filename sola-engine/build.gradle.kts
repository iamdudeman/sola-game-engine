plugins {
  id("sola.java-conventions")
}

dependencies {
  // TODO get from maven when it is published there
  api(files("libs/sola-ecs-2.0.3.jar"))
  api(files("libs/sola-json-2.1.1.jar"))
}

val fatJar = task("fatJar", type = Jar::class) {
  baseName = "${project.name}-fat"
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
