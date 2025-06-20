plugins {
  id("technology.sola.plugins.sola-android-conventions")
  id("com.android.library")
}

apply(plugin = "com.android.library")

dependencies {
  api(project(":sola-engine"))
}

android {
  namespace = "technology.sola.engine.platform.android"
  compileSdk = 35

  defaultConfig {
    minSdk = 34

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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

  publishing {
    singleVariant("release") {
      withSourcesJar()
      // note: this causes issue with publish for some reason
      // withJavadocJar()
    }
  }
}

publishing {
  publications {
    register<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "platform-android"

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
