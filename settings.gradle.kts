rootProject.name = "sola-game-engine"

pluginManagement {
  includeBuild("sola-gradle-plugins")
}

include(
  "sola-engine",
  "sola-engine:editor",
  "sola-engine:server",
  "sola-engine:platform:javafx", "sola-engine:platform:swing", "sola-engine:platform:browser",
  "examples:common", "examples:editor", "examples:server", "examples:javafx", "examples:swing", "examples:browser",
  "tooling"
)
