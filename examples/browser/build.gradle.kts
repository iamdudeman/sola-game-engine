plugins {
  id("sola.java-conventions")
}

dependencies {
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}

task("generateWebHtmlAndJs", JavaExec::class) {
  group = "build"

  dependsOn(tasks.getByPath("assemble"))

  classpath = sourceSets.main.get().runtimeClasspath
  setArgsString("build ${project.name}-${project.version}.jar")
  inputs.file("build/libs/${project.name}-${project.version}.jar")
  outputs.file("build/sola.js")
  mainClass.set("technology.sola.engine.examples.browser.GenerateBrowserFilesMain")
}

task("distWebZip", Zip::class) {
  group = "distribution"
  destinationDirectory.set(file(buildDir))
  archiveBaseName.set("examples-${project.name}")

  dependsOn(tasks.getByName("generateWebHtmlAndJs"))

  from("${project.rootDir}/assets") {
    into("assets")
  }
  from("${buildDir}/index.html")
  from("${buildDir}/sola.js")
}

tasks.assemble {
  finalizedBy(tasks.getByName("generateWebHtmlAndJs"))
}
