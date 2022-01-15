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
  configFile = file("../checkstyle.xml")
}

repositories {
  mavenCentral()
}

dependencies {
  // TODO possibly switch this back to implementation (need a JSON abstraction layer in place instead)
  api("com.google.code.gson:gson:2.8.7")

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
