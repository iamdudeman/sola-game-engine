plugins {
  id("sola.java-conventions")
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
  implementation(project(":examples:common"))
}

tasks.withType<Zip> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// todo update code to check for assets internally first, then externally
task("distFatJar", Jar::class) {
  group = "distribution"
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  val osClassifier = getOsClassifier()
  archiveBaseName.set("sola-engine-examples-${project.name}-${osClassifier}")

  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.javafx.JavaFxMain"
  }

  val dependencies = configurations.runtimeClasspath.get().map(::zipTree)

  from(dependencies)
  from("${project.rootDir}/assets") {
    into("assets")
  }
  with(tasks.jar.get())
  dependsOn(configurations.runtimeClasspath)
}

fun getOsClassifier(): String {
  if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_MAC)) {
    return "mac"
  } else if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_UNIX)) {
    return "linux"
  }

  return "win"
}
