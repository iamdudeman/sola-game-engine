package technology.sola.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*
import java.io.FileInputStream
import java.util.Properties


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

    val keystorePropertiesFile = project.rootProject.file("keystore.properties")
    val keystoreProperties = Properties()

    if (keystorePropertiesFile.exists()) {
      keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    }

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

      val storeFilePath = keystoreProperties["storeFile"] as String?

      if (storeFilePath == null) {
        System.err.println("Warning: 'storeFile' not found in 'keystore.properties'. Release build for Android will fail since it cannot sign the bundle.")
      }

      val isSigningDisabled = System.getenv("JITPACK") == "true"

      signingConfigs {
        if (!isSigningDisabled) {
          register("release") {
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
            storeFile = if (storeFilePath == null) null else java.io.File(storeFilePath)
            storePassword = keystoreProperties["storePassword"] as String?
          }
        }
      }

      buildTypes {
        debug {
          isMinifyEnabled = false
          isDebuggable = true
        }

        release {
          isMinifyEnabled = true
          proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
          signingConfig = if (isSigningDisabled) null else signingConfigs.getByName("release")
          isDebuggable = false
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

    project.tasks.register("ciBuild") {
      group = "build"

      dependsOn(project.tasks.named("assembleDebug"))
    }

    project.tasks.register("distAndroidDebugApk", Copy::class) {
      group = "distribution"

      from(project.file("build/outputs/apk/debug/android-debug.apk")) {
        rename { "${project.name}-${project.version}-debug.apk" }
      }

      into(project.file("${project.rootDir}/dist/${project.name}"))

      dependsOn(project.tasks.named("assembleDebug"))
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


