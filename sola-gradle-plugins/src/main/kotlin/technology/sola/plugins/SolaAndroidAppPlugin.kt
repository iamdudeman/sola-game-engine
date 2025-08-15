package technology.sola.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*


interface SolaAndroidAppPluginExtension {
}

class SolaAndroidAppPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    // uncomment if configuration is needed later
    // val solaAndroidPluginAppExtension = project.extensions.create<SolaAndroidConventionsPluginExtension>("solaAndroidApp")

    project.repositories {
      mavenCentral()
      google()

      maven {
        url = project.uri("https://jitpack.io")
      }
    }

    project.pluginManager.apply("com.android.application")

    project.extensions.configure<BaseAppModuleExtension> {
      namespace = "${project.properties["basePackage"]}.${project.name}"
      compileSdk = 35

      defaultConfig {
        applicationId = "${project.properties["basePackage"]}.${project.name}"
        versionCode = project.properties["androidVersionCode"].toString().toInt()
        versionName = "${project.properties["version"]}"
        minSdk = 34
        targetSdk = 35
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      }

      buildTypes {
        release {
          isMinifyEnabled = false
          proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
      }

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
      }

      sourceSets {
        getByName("main") {
          assets.srcDir("${project.rootDir}/assets")
        }
      }
    }

    project.tasks.register("distAndroidDebugApk", Copy::class) {
      group = "distribution"

      from(project.file("build/outputs/apk/debug/android-debug.apk")) {
        rename { "${project.name}-${project.version}-debug.apk" }
      }

      into(project.file("${project.rootDir}/dist/${project.name}"))
    }

    project.tasks.register("distAndroidReleaseBundle", Copy::class) {
      group = "distribution"

      dependsOn(project.tasks.named("bundleRelease"))

      from(project.file("build/outputs/bundle/release/android-release.aab")) {
        rename { "${project.name}-${project.version}-release.aab" }
      }

      into(project.file("${project.rootDir}/dist/${project.name}"))
    }
  }
}


