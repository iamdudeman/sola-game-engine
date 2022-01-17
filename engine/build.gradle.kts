plugins {
  id("java-library")
  checkstyle
  jacoco
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(16))
  }
}

checkstyle {
  configFile = file("../checkstyle.xml")
}

repositories {
  mavenCentral()
}

dependencies {
  // TODO get from maven when it is published there
  api(files("libs/sola-json-1.0.1.jar"))

  // Logging
  implementation("org.slf4j:slf4j-log4j12:1.7.30")

  // Test
  testImplementation("org.mockito:mockito-inline:4.2.0")
  testImplementation("org.mockito:mockito-junit-jupiter:4.2.0")
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
