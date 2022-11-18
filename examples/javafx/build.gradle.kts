plugins {
  id("sola.java-conventions")
}

dependencies {
  implementation(project(":sola-engine:platform:javafx"))
  implementation(project(":examples:common"))

  val osClassifier = getOsClassifier()

  runtimeOnly("org.openjfx", "javafx-base", "17.0.2", classifier = osClassifier)
  runtimeOnly("org.openjfx", "javafx-controls", "17.0.2", classifier = osClassifier)
  runtimeOnly("org.openjfx", "javafx-graphics", "17.0.2", classifier = osClassifier)
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

  val osClassifier = getOsClassifier()
  archiveBaseName.set("examples-${project.name}-${osClassifier}")

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
