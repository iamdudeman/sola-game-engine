plugins {
  id("sola.java-conventions")
  id("technology.sola.sola-java-distribution")
}

dependencies {
  implementation(project(":sola-engine:server"))
  implementation(project(":examples:common"))
}

solaJavaDist {
  mainClass = "${project.properties["basePackage"]}.${project.name}.ServerMain"
}
