plugins {
  // todo figure out how to create apk (signed and unsigned??)
  // id("technology.sola.plugins.sola-java-distribution")

  id("technology.sola.plugins.sola-android-conventions")
  id("technology.sola.plugins.sola-android-app")
}

dependencies {
  implementation(project(":sola-engine:platform:android"))
  implementation(project(":examples:common"))
}

//solaJavaDist {
//  mainClass = "${project.properties["basePackage"]}.${project.name}.AndroidMain"
//}
