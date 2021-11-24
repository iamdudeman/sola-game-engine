package technology.sola.engine.core;

public class SolaConfiguration {
  private final String solaTitle;
  private final int canvasWidth;
  private final int canvasHeight;
  private final int gameLoopTargetUpdatesPerSecond;
  private final boolean isGameLoopRestingAllowed;

  public SolaConfiguration(String solaTitle, int canvasWidth, int canvasHeight, int gameLoopTargetUpdatesPerSecond, boolean isGameLoopRestingAllowed) {
    this.solaTitle = solaTitle;
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;
    this.gameLoopTargetUpdatesPerSecond = gameLoopTargetUpdatesPerSecond;
    this.isGameLoopRestingAllowed = isGameLoopRestingAllowed;
  }

  public String getSolaTitle() {
    return solaTitle;
  }

  public int getCanvasWidth() {
    return canvasWidth;
  }

  public int getCanvasHeight() {
    return canvasHeight;
  }

  public int getGameLoopTargetUpdatesPerSecond() {
    return gameLoopTargetUpdatesPerSecond;
  }

  public boolean isGameLoopRestingAllowed() {
    return isGameLoopRestingAllowed;
  }
}
