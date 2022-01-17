plugins {
  id("sola.java-conventions")
}

tasks.jar {
  archiveBaseName.set("sola-engine-${project.name}")
}

dependencies {
  api(project(":sola-engine"))
}
