rootProject.name = "sola-game-engine"

include(
  "sola-engine",
  "sola-engine:server",
  "sola-engine:platform:javafx", "sola-engine:platform:swing", "sola-engine:platform:browser",
  "examples:server", "examples:common", "examples:javafx", "examples:swing", "examples:browser",
  "tooling"
)
