plugins {
  // id("technology.sola.sola-publishing")

  id("com.android.library") version "8.10.1" apply true
}

dependencies {
  api(project(":sola-engine"))

  // nullability annotations
  implementation("org.jspecify:jspecify:1.0.0")

  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
}

// todo
//solaPublishing {
//  artifactId = "platform-android"
//}

android {
  namespace = "com.example.mylibrary"
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

repositories {
  mavenCentral()
  google()

  maven {
    url = uri("https://jitpack.io")
  }
}
