package technology.sola.engine.core;

public record SolaConfiguration(
  String solaTitle,
  int canvasWidth, int canvasHeight,
  int gameLoopTargetUpdatesPerSecond, boolean isGameLoopRestingAllowed) {
}
