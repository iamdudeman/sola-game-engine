plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.plugins.sola-web-distribution")
}

dependencies {
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}
