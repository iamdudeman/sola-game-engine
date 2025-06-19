subprojects {
  configure(allprojects - project(":examples:android")) {
    apply(plugin = "sola.java-conventions")
  }

  apply(plugin = "maven-publish")

  val publishTask = project.tasks.getByName("publishToMavenLocal")

  gradle.includedBuilds.forEach { build ->
    publishTask.dependsOn(build.task(":publishToMavenLocal"))
  }
}
