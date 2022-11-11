plugins {
  id("application")
}

dependencies {
  implementation(project(":sola-engine:platform:swing"))
  implementation(project(":examples:common"))
}

tasks.withType<Zip>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task("distFatJar", type = Jar::class) {
  group = "distribution"
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  archiveBaseName.set("sola-engine-examples-${project.name}")

  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.swing.SwingMain"
  }

  val dependencies = configurations.runtimeClasspath.get().map(::zipTree)

  from(dependencies)
  dependsOn(configurations.runtimeClasspath)
  with(tasks.jar.get())
}
