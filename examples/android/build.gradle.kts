plugins {
  // id("sola.java-conventions")
  // id("technology.sola.plugins.sola-java-distribution")
//  checkstyle
//  jacoco

  id("com.android.application") version "8.10.1" apply true
}

// todo missing checkstyle
// todo missing jacoco
// todo missing junit test stuff

dependencies {
  implementation(project(":sola-engine:platform:android"))
  implementation(project(":examples:common"))

  // nullability annotations
  implementation("org.jspecify:jspecify:1.0.0")

  // unit testing
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")

   implementation("androidx.appcompat:appcompat:1.6.1")
   implementation("com.google.android.material:material:1.10.0")
   implementation("androidx.activity:activity:1.8.0")
   implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}

//solaJavaDist {
//  mainClass = "${project.properties["basePackage"]}.${project.name}.AndroidMain"
//}

//checkstyle {
//  configFile = file("$rootDir/checkstyle.xml")
//}

//jacoco {
//  toolVersion = "0.8.8"
//}

android {
  namespace = "technology.sola.engine.examples.android"
  compileSdk = 34

  defaultConfig {
    applicationId = "cotechnology.sola.engine.examples.android"
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

repositories {
  mavenCentral()
  google()

  maven {
    url = uri("https://jitpack.io")
  }
}
