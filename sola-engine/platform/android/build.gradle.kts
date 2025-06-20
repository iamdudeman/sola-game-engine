plugins {
  // todo figure out how to publish this platform for sola game template to use
  // id("technology.sola.sola-publishing")

  id("technology.sola.plugins.sola-android")
  id("com.android.library") version "8.10.1" apply true
}

dependencies {
  api(project(":sola-engine"))
}

solaAndroid {
//  isLibrary = true
}

// todo
//solaPublishing {
//  artifactId = "platform-android"
//}

android {
  namespace = "technology.sola.engine.platform.android"
  compileSdk = 34

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
}

