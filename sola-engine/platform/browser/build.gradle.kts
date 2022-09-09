plugins {
  id("sola.platform-conventions")
}

dependencies {
  // Note: TeaVM:2b671b8088 contains needed features [records, Random#nextInt(origin, bounds), Random#nextFloat(origin, bounds), Thread#setName]
  implementation("com.github.konsoletyper.teavm:teavm-classlib:2b671b8088")
  implementation("com.github.konsoletyper.teavm:teavm-tooling:2b671b8088")
  implementation("org.teavm:teavm-extras-slf4j:0.7.0")
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
