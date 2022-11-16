plugins {
  id("sola.java-conventions")
}

dependencies {
  implementation(project(":sola-engine:platform:swing"))
  implementation(project(":examples:common"))
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

  archiveBaseName.set("examples-${project.name}")

  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.swing.SwingMain"
  }

  val dependencies = configurations.runtimeClasspath.get().map(::zipTree)

  from(dependencies)
  from("${project.rootDir}/assets") {
    into("assets")
  }
  with(tasks.jar.get())
  dependsOn(configurations.runtimeClasspath)
}
