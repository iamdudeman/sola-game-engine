plugins {
  id("java-library")
  id("io.github.zebalu.teavm-gradle-plugin") version "1.0.0"
}

repositories {
  mavenCentral()
}

buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath("io.github.zebalu:teavm-gradle-plugin:+")
  }
}

dependencies {
  api(project(":platform:browser"))
  api(project(":examples:common"))
}

teavm {
  mainClass = "technology.sola.engine.examples.browser.Main"
  sourceDirectory = file("src")
  targetDirectory = file("build")
  targetFileName = "sola.js"

  isMinifying = false
  targetType = org.teavm.tooling.TeaVMTargetType.JAVASCRIPT
}


tasks.withType<Jar>() {
  manifest {
    attributes["Main-Class"] = "technology.sola.engine.examples.Main"
  }

  dependsOn(configurations.runtimeClasspath)

  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}

tasks.withType<io.github.zebalu.gradle.teavm.TeavmCompileTask> {
  dependsOn(":examples:browser:assemble")
}
