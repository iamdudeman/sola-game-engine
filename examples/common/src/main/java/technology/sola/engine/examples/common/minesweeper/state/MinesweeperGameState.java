package technology.sola.engine.examples.common.minesweeper.state;

public class MinesweeperGameState {
  private static MinesweeperField currentField;

  public static MinesweeperField getCurrentField() {
    return currentField;
  }

  public static void setCurrentField(MinesweeperField currentField) {
    MinesweeperGameState.currentField = currentField;
  }
}
