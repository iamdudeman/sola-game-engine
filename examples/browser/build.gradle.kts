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
    classpath("io.github.zebalu:teavm-gradle-plugin:1.0.0")
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

  isMinifying = true
  isSourceMapsGenerated = false
  isSourceFilesCopied = false
  targetType = org.teavm.tooling.TeaVMTargetType.JAVASCRIPT
  optimizationLevel = org.teavm.vm.TeaVMOptimizationLevel.ADVANCED
}

tasks.withType<io.github.zebalu.gradle.teavm.TeavmCompileTask> {
  dependsOn(":examples:browser:assemble")
}