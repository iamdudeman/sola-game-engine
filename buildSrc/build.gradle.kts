plugins {
  `kotlin-dsl`
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
      id = "technology.sola.sola-java-distribution"
      implementationClass = "SolaJavaDistributionPlugin"
    }

    create("sola-web-distribution") {
      id = "technology.sola.sola-web-distribution"
      implementationClass = "SolaWebDistributionPlugin"
    }

    create("sola-publishing") {
      id = "technology.sola.sola-publishing"
      implementationClass = "SolaPublishingPlugin"
    }
  }
}
