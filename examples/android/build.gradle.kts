plugins {
  // todo figure out how to create apk (signed and unsigned??)
  // id("technology.sola.plugins.sola-java-distribution")

  id("technology.sola.plugins.sola-android")
  id("com.android.application") version "8.10.1" apply true
}

dependencies {
  implementation(project(":sola-engine:platform:android"))
  implementation(project(":examples:common"))
}

solaAndroid {

}

//solaJavaDist {
//  mainClass = "${project.properties["basePackage"]}.${project.name}.AndroidMain"
//}

android {
  namespace = "technology.sola.engine.examples.android"
  compileSdk = 34

  defaultConfig {
    applicationId = "technology.sola.engine.examples.android"
    minSdk = 34
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

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
}

