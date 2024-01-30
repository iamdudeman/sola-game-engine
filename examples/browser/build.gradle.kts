plugins {
  id("sola.java-conventions")
  id("technology.sola.sola-web-distribution")
}

dependencies {
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}
