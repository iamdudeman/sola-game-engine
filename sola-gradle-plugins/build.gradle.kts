import java.util.*

// todo probably need publishing stuff as well

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
}

val props = Properties()

props.load(file("../gradle.properties").inputStream())

group = "technology.sola.plugins"
version = props.getProperty("version")

repositories {
  gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
  implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.7.2")
}

gradlePlugin {
  plugins {
    create("sola-java-distribution") {
      id = "technology.sola.plugins.sola-java-distribution"
      implementationClass = "technology.sola.plugins.SolaJavaDistributionPlugin"
    }

    create("sola-web-distribution") {
      id = "technology.sola.plugins.sola-web-distribution"
      implementationClass = "technology.sola.plugins.SolaWebDistributionPlugin"
    }
  }
}
