plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}

tasks.register<JavaExec>("generateBrowserExampleHtmlAndJs") {
  group = "build"

  dependsOn(tasks.getByPath("assemble"))

  classpath = sourceSets.main.get().runtimeClasspath
  setArgsString("build ${project.name}-${project.version}.jar")
  mainClass.set("technology.sola.engine.examples.browser.GenerateBrowserFilesMain")
}
