plugins {
  id("java-library")
  checkstyle
  jacoco
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  withSourcesJar()
  withJavadocJar()
}

checkstyle {
  configFile = file("$rootDir/checkstyle.xml")
}

repositories {
  mavenCentral()

  maven {
    url = uri("https://jitpack.io")
  }
}

dependencies {
  // Logging
  implementation("org.slf4j:slf4j-api:2.0.12")
  implementation("org.slf4j:slf4j-reload4j:2.0.12")

  // Test
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

jacoco {
  toolVersion = "0.8.8"
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks.jacocoTestReport {
  reports {
    html.required.set(true)
    html.outputLocation.set(file("${layout.buildDirectory}/reports/coverage"))
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
