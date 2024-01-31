plugins {
  id("sola.java-conventions")
  id("technology.sola.plugins.sola-java-distribution")
}

dependencies {
  implementation(project(":sola-engine:platform:swing"))
  implementation(project(":examples:common"))
}

solaJavaDist {
  mainClass = "${project.properties["basePackage"]}.${project.name}.SwingMain"
}
