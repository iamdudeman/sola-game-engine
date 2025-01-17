package technology.sola.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.*
import java.util.Calendar

interface SolaJavaDistributionPluginExtension {
  var mainClass: String?
  var useJavaFx: Boolean?
}

class SolaJavaDistributionPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val solaJavaDistributionPluginExtension = project.extensions.create<SolaJavaDistributionPluginExtension>("solaJavaDist")

    project.afterEvaluate {
      val osClassifier = getOsClassifier()
      val osClassifierWithDash = if (solaJavaDistributionPluginExtension.useJavaFx != true) {
        ""
      } else {
        "-$osClassifier"
      }

      if (solaJavaDistributionPluginExtension.useJavaFx == true) {
        project.dependencies.add("runtimeOnly", "org.openjfx:javafx-base:${project.properties["javaFxVersion"]}:${osClassifier}")
        project.dependencies.add("runtimeOnly", "org.openjfx:javafx-controls:${project.properties["javaFxVersion"]}:${osClassifier}")
        project.dependencies.add("runtimeOnly", "org.openjfx:javafx-graphics:${project.properties["javaFxVersion"]}:${osClassifier}")
      }

      project.tasks.withType<Zip> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }

      project.tasks.withType<Tar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }

      project.task("cleanDist") {
        group = "distribution"

        doFirst {
          delete("${project.rootDir}/dist")
        }
      }

      project.tasks.getByName("clean").dependsOn("cleanDist")

      project.task("distFatJar", Jar::class) {
        group = "distribution"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveBaseName.set("${project.properties["gameName"]}-${project.name}${osClassifierWithDash}")

        manifest {
          attributes["Main-Class"] = solaJavaDistributionPluginExtension.mainClass
        }

        val dependencies = configurations.getByName("runtimeClasspath").map(::zipTree)

        from(dependencies)
        from("${project.rootDir}/assets") {
          into("assets")
        }
        with(project.tasks.getByName("jar", CopySpec::class))
        destinationDirectory.set(file("${project.rootDir}/dist/${project.name}"))
        dependsOn(configurations.getByName("runtimeClasspath"))
      }

      project.task("prepareJPackage", Delete::class) {
        delete("${layout.buildDirectory}/jpackage")
      }

      project.task("distWinJPackage", Exec::class) {
        group = "distribution"
        dependsOn(tasks.getByName("prepareJPackage"))
        dependsOn(tasks.getByName("distFatJar"))

        executable("jpackage")

        args(
          "--name", "${project.properties["gameName"]}-${project.version}",
          "--app-version", "${project.version}",
          "--vendor", project.properties["vendor"],
          "--icon", "${project.rootDir}/assets/icon.ico",
          "--copyright", "©${Calendar.getInstance().get(Calendar.YEAR)} ${project.properties["vendor"]}. All rights reserved.",
          "--dest", "${layout.buildDirectory}/jpackage",
          "--input", "${project.rootDir}/dist/${project.name}",
          "--main-jar", "${project.properties["gameName"]}-${project.name}${osClassifierWithDash}-${project.version}.jar",
          "--type", "app-image"
        )
      }

      project.task("distWinJPackageZip", Zip::class) {
        group = "distribution"
        destinationDirectory.set(file("${project.rootDir}/dist/${project.name}"))
        archiveBaseName.set("${project.properties["gameName"]}-${project.name}-win")

        dependsOn(tasks.getByName("distWinJPackage"))

        from("${layout.buildDirectory}/jpackage/${project.properties["gameName"]}-${project.version}")
      }
    }
  }
}

fun getOsClassifier(): String {
  if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_MAC)) {
    return "mac"
  } else if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_UNIX)) {
    return "linux"
  }

  return "win"
}
