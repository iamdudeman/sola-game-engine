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
  implementation(project(":sola-engine:platform:browser"))
  implementation(project(":examples:common"))
}

teavm {
  mainClass = "technology.sola.engine.examples.browser.BrowserMain"
  sourceDirectory = file("src")
  targetDirectory = file("build")
  targetFileName = "sola.js"

  isMinifying = false
  isSourceMapsGenerated = false
  isSourceFilesCopied = false
  targetType = org.teavm.tooling.TeaVMTargetType.JAVASCRIPT
  optimizationLevel = org.teavm.vm.TeaVMOptimizationLevel.ADVANCED
}

tasks.withType<io.github.zebalu.gradle.teavm.TeavmCompileTask> {
  dependsOn(":examples:browser:assemble")
}
