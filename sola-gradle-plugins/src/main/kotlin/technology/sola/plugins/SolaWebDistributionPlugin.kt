package technology.sola.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

interface SolaWebDistributionPluginExtension {
  var generateFilesMainClass: String?
}

class SolaWebDistributionPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val solaWebDistributionPluginExtension = project.extensions.create<SolaWebDistributionPluginExtension>("solaWebDist")
    val javaPluginExtension = project.extensions.getByType<JavaPluginExtension>()
    val sourceSets = javaPluginExtension.sourceSets

    project.afterEvaluate {
      val generateFilesMainClass = if (solaWebDistributionPluginExtension.generateFilesMainClass == null) {
        "${project.properties["basePackage"]}.browser.GenerateBrowserFilesMain"
      } else {
        solaWebDistributionPluginExtension.generateFilesMainClass
      }

      project.tasks.register("cleanDist") {
        group = "sola"

        doFirst {
          delete("${project.rootDir}/dist")
        }
      }

      project.tasks.getByName("clean").dependsOn("cleanDist")

      project.tasks.getByName("buildCi").dependsOn("buildWebHtmlAndJs")

      project.tasks.register("buildWebHtmlAndJs", JavaExec::class.java) {
        group = "sola"

        dependsOn(project.tasks.getByPath("assemble"))

        classpath = sourceSets.getByName("main").runtimeClasspath
        setArgsString("build ${project.name}-${project.version}.jar")
        inputs.file("build/libs/${project.name}-${project.version}.jar")
        outputs.file("build/sola.js")
        mainClass.set(generateFilesMainClass)
      }

      project.tasks.register("buildDebugWebHtmlAndJs") {
        group = "sola"

        doFirst {
          project.tasks.withType<JavaExec> {
            setArgsString("build ${project.name}-${project.version}.jar debug")
          }
        }

        finalizedBy(project.tasks.getByPath("buildWebHtmlAndJs"))
      }

      project.tasks.register("distWebZip", Zip::class.java) {
        group = "sola"
        destinationDirectory.set(project.file("${project.rootDir}/dist/${project.name}"))
        archiveBaseName.set("${project.properties["gameName"]}-${project.name}")

        dependsOn(project.tasks.getByName("buildWebHtmlAndJs"))

        from("${project.rootDir}/assets") {
          into("assets")
        }

        from(layout.buildDirectory.file("index.html"))
        from(layout.buildDirectory.file("sola.js"))
      }

      project.tasks.getByName("assemble").finalizedBy(project.tasks.getByName("buildWebHtmlAndJs"))
    }
  }
}
