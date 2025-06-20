package technology.sola.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

interface SolaAndroidPluginExtension {
  var disableCheckstyle: Boolean?
  var disableCoverage: Boolean?
  var isLibrary: Boolean?
}

class SolaAndroidPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val solaAndroidPluginExtension = project.extensions.create<SolaAndroidPluginExtension>("solaAndroid")

    project.repositories {
      mavenCentral()
      google()

      maven {
        url = project.uri("https://jitpack.io")
      }
    }

//    project.afterEvaluate {
//      if (solaAndroidPluginExtension.isLibrary == true) {
//        project.pluginManager.apply("com.android.library:8.10.1")
//      } else {
//        project.pluginManager.apply("com.android.application:8.10.1")
//      }
//    }

    project.afterEvaluate {
      // nullability annotations
      project.dependencies.add("api", "org.jspecify:jspecify:1.0.0")

      // unit testing
      project.dependencies.add("testImplementation", "org.mockito:mockito-inline:5.2.0")
      project.dependencies.add("testImplementation", "org.mockito:mockito-junit-jupiter:5.11.0")
      project.dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter:5.11.0")

      // android
      project.dependencies.add("implementation", "androidx.appcompat:appcompat:1.6.1")
      project.dependencies.add("implementation", "com.google.android.material:material:1.10.0")
      project.dependencies.add("implementation", "androidx.activity:activity:1.8.0")
      project.dependencies.add("implementation", "androidx.constraintlayout:constraintlayout:2.1.4")
    }

    project.tasks.withType<Test> {
      useJUnitPlatform()
      testLogging {
        events("passed", "skipped", "failed")
      }
    }

    project.afterEvaluate {
      if (solaAndroidPluginExtension.disableCheckstyle != true) {
        project.pluginManager.apply(CheckstylePlugin::class.java)

        project.extensions.configure<CheckstyleExtension> {
          configFile = project.file(project.rootDir.toString() + "/checkstyle.xml")
        }

        project.task("checkstyleMain", Checkstyle::class) {
          group = "verification"

          configFile = project.file(project.rootDir.toString() + "/checkstyle.xml")
          source("src")
          include("**/*.java")
          exclude("**/gen/**")
          classpath = files()
        }

        project.tasks.getByName("check").dependsOn("checkstyleMain")
      }

      if (solaAndroidPluginExtension.disableCoverage != true) {
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


