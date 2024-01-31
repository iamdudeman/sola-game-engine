import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType

interface SolaPublishingPluginExtension {
  var artifactId: String?
}

class SolaPublishingPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val solaPublishingPluginExtension = project.extensions.create<SolaPublishingPluginExtension>("solaPublishing")
    val publishingPluginExtension = project.extensions.getByType<PublishingExtension>()

    project.afterEvaluate {
      publishingPluginExtension.publications {
        create<MavenPublication>("maven") {
          group = "technology.sola.engine"
          artifactId = solaPublishingPluginExtension.artifactId

          from(components["java"])
        }
      }
    }
  }
}
