plugins {
  id("java-library")
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

dependencies {
  api(project(":engine"))
  implementation("org.teavm:teavm-jso-apis:0.6.1")

  // Logging
  implementation("org.teavm:teavm-extras-slf4j:0.6.1")

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
