plugins {
  id("technology.sola.plugins.sola-java-conventions")
  id("technology.sola.plugins.sola-java-distribution")
}

dependencies {
  implementation(project(":sola-engine:server"))
  implementation(project(":examples:common"))
}

solaJavaDist {
  mainClass = "${project.properties["basePackage"]}.${project.name}.ServerMain"
}
