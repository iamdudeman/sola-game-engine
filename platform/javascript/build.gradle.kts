plugins {
  id("java-library")
  id("io.github.zebalu.teavm-gradle-plugin") version "1.0.0"
  checkstyle
  jacoco
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

checkstyle {
  configFile = file("../../checkstyle.xml")
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

//tasks.withType<teavm> {
//  mainClass = "temp.Main"
//  sourceDirectory = file("src")
//  targetDirectory = file("build/teavm")
//  targetFileName = "java_lib.js"
//
//  targetType = "JAVASCRIPT"
//}


teavm {
  mainClass = "technology.sola.engine.platform.js.TestMain"
  sourceDirectory = file("src")
  targetDirectory = file("build")
  targetFileName = "sola.js"

  targetType = org.teavm.tooling.TeaVMTargetType.JAVASCRIPT
}

dependencies {
  api(project(":engine"))
  implementation("org.teavm:teavm-jso-apis:0.6.1")

  // Logging
  implementation("org.slf4j:slf4j-log4j12:1.7.30")

  // Test
  testImplementation("org.mockito:mockito-inline:3.0.0")
  testImplementation("org.mockito:mockito-junit-jupiter:3.0.0")
  testImplementation(platform("org.junit:junit-bom:5.7.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks.jacocoTestReport {
  reports {
    html.isEnabled = true
    html.destination = file("$buildDir/reports/coverage")
  }
}

tasks.jacocoTestCoverageVerification {
  violationRules {
    rule {
      limit {
        minimum = "0.8".toBigDecimal()
      }
    }
  }
}

tasks.withType<Jar> {
  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
