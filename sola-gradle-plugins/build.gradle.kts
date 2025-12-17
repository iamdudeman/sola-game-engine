import java.util.*

plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
  id("com.gradle.plugin-publish") version "2.0.0"
}

val props = Properties()

props.load(file("../gradle.properties").inputStream())

group = "technology.sola.plugins"
version = props.getProperty("version")

repositories {
  google()
  gradlePluginPortal() // so that external plugins can be resolved in dependencies
}

dependencies {
  implementation("com.android.tools.build:gradle:8.12.3")
}

gradlePlugin {
  plugins {
    create("sola-android-app") {
      id = "technology.sola.plugins.sola-android-app"
      implementationClass = "technology.sola.plugins.SolaAndroidAppPlugin"
    }

    create("sola-android-conventions") {
      id = "technology.sola.plugins.sola-android-conventions"
      implementationClass = "technology.sola.plugins.SolaAndroidConventionsPlugin"
    }

    create("sola-java-conventions") {
      id = "technology.sola.plugins.sola-java-conventions"
      implementationClass = "technology.sola.plugins.SolaJavaConventionsPlugin"
    }

    create("sola-javafx") {
      id = "technology.sola.plugins.sola-javafx"
      implementationClass = "technology.sola.plugins.SolaJavaFxPlugin"
    }

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
