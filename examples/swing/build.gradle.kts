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
    attributes["Main-Class"] = "${project.properties["basePackage"]}.${project.name}.SwingMain"
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
    "--main-jar", "${project.properties["gameName"]}-${project.name}-${project.version}.jar",
    "--type", "app-image"
  )
}

task("distWinJPackageZip", Zip::class) {
  group = "distribution"
  destinationDirectory.set(file("$buildDir/jpackage"))
  archiveBaseName.set("${project.properties["gameName"]}-win-${project.name}")

  dependsOn(tasks.getByName("distWinJPackage"))

  from("${buildDir}/jpackage/${project.properties["gameName"]}-${project.version}")
}
