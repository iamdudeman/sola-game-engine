plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
}

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
      implementationClass = "SolaJavaDistributionPlugin"
    }

    create("sola-web-distribution") {
      id = "technology.sola.plugins.sola-web-distribution"
      implementationClass = "SolaWebDistributionPlugin"
    }
  }
}
