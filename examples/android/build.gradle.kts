plugins {
  id("technology.sola.plugins.sola-android-conventions")
  id("technology.sola.plugins.sola-android-app")
}

dependencies {
  implementation(project(":sola-engine:platform:android"))
  implementation(project(":examples:common"))
}
