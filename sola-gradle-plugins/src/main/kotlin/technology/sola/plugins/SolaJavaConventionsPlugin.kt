package technology.sola.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

interface SolaJavaConventionsPluginExtension {
  var disableCheckstyle: Boolean?
  var disableCoverage: Boolean?
}

class SolaJavaConventionsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
   val solaJavaConventionsPluginExtension = project.extensions.create<SolaJavaConventionsPluginExtension>("solaJavaConventions")

    project.pluginManager.apply("java-library")

    project.afterEvaluate {
      // nullability annotations
      project.dependencies.add("api", "org.jspecify:jspecify:1.0.0")

      // unit testing
      project.dependencies.add("testImplementation", "org.mockito:mockito-inline:5.2.0")
      project.dependencies.add("testImplementation", "org.mockito:mockito-junit-jupiter:5.11.0")
      project.dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter:5.11.0")
      project.dependencies.add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    project.repositories {
      mavenCentral()

      maven {
        url = project.uri("https://jitpack.io")
      }
    }

    project.extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
      }

      withSourcesJar()
      withJavadocJar()
    }

    project.tasks.withType<Test> {
      useJUnitPlatform()
      testLogging {
        events("passed", "skipped", "failed")
      }
    }

    project.afterEvaluate {
      project.tasks.register("ciBuild") {
        group = "build"

        dependsOn(project.tasks.named("build"))
      }

      if (solaJavaConventionsPluginExtension.disableCheckstyle != true) {
        project.pluginManager.apply("checkstyle")

        project.extensions.configure<CheckstyleExtension> {
          configFile = project.file(project.rootDir.toString() + "/checkstyle.xml")
        }
      }

      if (solaJavaConventionsPluginExtension.disableCoverage != true) {
        project.pluginManager.apply("jacoco")

        project.extensions.configure<JacocoPluginExtension> {
          toolVersion = "0.8.8"
        }

        project.tasks.withType<JacocoReport> {
          reports {
            html.required.set(true)
            html.outputLocation.set(project.file(project.layout.buildDirectory.file("reports/coverage")))
          }
        }

        project.tasks.withType<JacocoCoverageVerification> {
          violationRules {
            rule {
              limit {
                minimum = "0.8".toBigDecimal()
              }
            }
          }
        }
      }
    }
  }
}


