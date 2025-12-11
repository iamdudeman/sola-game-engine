package technology.sola.plugins

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

interface SolaJavaFxPluginExtension {
}

class SolaJavaFxPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val solaJavaFxPluginExtension = project.extensions.create<SolaJavaFxPluginExtension>("solaJavaFx")
    val osClassifier = getOsClassifier()

    project.dependencies.add("runtimeOnly", "org.openjfx:javafx-base:${project.properties["javaFxVersion"]}:${osClassifier}")
    project.dependencies.add("runtimeOnly", "org.openjfx:javafx-controls:${project.properties["javaFxVersion"]}:${osClassifier}")
    project.dependencies.add("runtimeOnly", "org.openjfx:javafx-media:${project.properties["javaFxVersion"]}:${osClassifier}")
    project.dependencies.add("runtimeOnly", "org.openjfx:javafx-graphics:${project.properties["javaFxVersion"]}:${osClassifier}")
  }

  fun getOsClassifier(): String {
    if (Os.isFamily(Os.FAMILY_MAC)) {
      return "mac"
    } else if (Os.isFamily(Os.FAMILY_UNIX)) {
      return "linux"
    }

    return "win"
  }
}
