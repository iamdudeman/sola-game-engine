plugins {
  id("java-library")
  id("org.openjfx.javafxplugin") version "0.0.9"
  checkstyle
  jacoco
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

javafx {
  modules("javafx.controls", "javafx.fxml")
  configuration = "api" // TODO this might have issues on other platforms (possibly need reliant code in separate module)
}

checkstyle {
  configFile = file("../../checkstyle.xml")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":engine"))

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
