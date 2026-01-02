plugins {
  id("technology.sola.plugins.sola-java-conventions")
}

dependencies {
  // can also point to git branch via commit (ex. 8d601c3cca)
  api("com.github.iamdudeman:sola-ecs:2.3.1")
  api("com.github.iamdudeman:sola-json:4.1.0")

  // performance testing dependencies
  testImplementation("org.openjdk.jmh:jmh-core:1.37")
  testAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      group = "technology.sola.engine"
      artifactId = "sola-engine"

      from(components["java"])
    }
  }
}

tasks.register("jmhBenchmark", JavaExec::class) {
  group = "sola"
  description = "Execute jmh benchmark comparisons"
  mainClass = "org.openjdk.jmh.Main"

  classpath = sourceSets.test.get().runtimeClasspath
}
