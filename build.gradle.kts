subprojects {
  apply(plugin = "maven-publish")

  val publishTask = project.tasks.getByName("publishToMavenLocal")

  gradle.includedBuilds.forEach { build ->
    publishTask.dependsOn(build.task(":publishToMavenLocal"))
  }

  tasks.withType<JavaExec>().configureEach {
    if (name.endsWith("main()")) {
      notCompatibleWithConfigurationCache("JavaExec created by IntelliJ")
    }
  }
}
