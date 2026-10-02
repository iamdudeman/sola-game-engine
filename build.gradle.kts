subprojects {
  apply(plugin = "maven-publish")

  plugins.withId("java-library") {
    extensions.configure<PublishingExtension> {
      publications {
        if (findByName("mavenJava") == null) {
          create<MavenPublication>("mavenJava") {
            from(components["java"])
          }
        }
      }
    }
  }

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
