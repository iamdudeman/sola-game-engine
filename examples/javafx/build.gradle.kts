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
    attributes["Main-Class"] = "${project.properties["basePackage"]}.${project.name}.JavaFxMain"
  }

  val dependencies = configurations.runtimeClasspath.get().map(::zipTree)

  from(dependencies)
  from("${project.rootDir}/assets") {
    into("assets")
  }
  with(tasks.jar.get())
  destinationDirectory.set(file("$buildDir/dist"))
  dependsOn(configurations.runtimeClasspath)
}

task("prepareJPackage", Delete::class) {
  delete("$buildDir/jpackage")
}

task("distWinJPackage", Exec::class) {
  group = "distribution"
  dependsOn(tasks.getByName("prepareJPackage"))
  dependsOn(tasks.getByName("distFatJar"))

  executable("jpackage")

  args(
    "--name", "${project.properties["gameName"]}-${project.version}",
    "--app-version", "${project.version}",
    "--vendor", project.properties["vendor"],
    "--dest", "$buildDir/jpackage",
    "--input", "$buildDir/dist",
    "--main-jar", "${project.properties["gameName"]}-${project.name}-win-${project.version}.jar",
    "--type", "app-image"
  )
}

task("distWinJPackageZip", Zip::class) {
  group = "distribution"
  destinationDirectory.set(file("$buildDir/jpackage"))
  archiveBaseName.set("${project.properties["gameName"]}-win-${project.name}")

  dependsOn(tasks.getByName("distWinJPackage"))

  from("${buildDir}/jpackage/${project.properties["gameName"]}")
}

fun getOsClassifier(): String {
  if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_MAC)) {
    return "mac"
  } else if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_UNIX)) {
    return "linux"
  }

  return "win"
}
