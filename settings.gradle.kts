rootProject.name = "sola-game-engine"

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }

  includeBuild("sola-gradle-plugins")
}

include(
  "sola-engine",
  "sola-engine:editor",
  "sola-engine:server",
  "sola-engine:platform:javafx", "sola-engine:platform:swing", "sola-engine:platform:browser", "sola-engine:platform:android",
  "examples:common", "examples:editor", "examples:server", "examples:javafx", "examples:swing", "examples:browser", "examples:android",
  "tooling"
)
