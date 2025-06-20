plugins {
  // todo figure out how to create apk (signed and unsigned??)
  // id("technology.sola.plugins.sola-java-distribution")

  id("technology.sola.plugins.sola-android")
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
  namespace = "${project.properties["basePackage"]}.${project.name}"

  defaultConfig {
    applicationId = "${project.properties["basePackage"]}.${project.name}"
    versionCode = project.properties["androidVersionCode"].toString().toInt()
    versionName = "${project.properties["version"]}"
  }
}

