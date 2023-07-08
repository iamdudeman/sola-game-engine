package technology.sola.engine.examples.common.minesweeper.components;

import technology.sola.ecs.Component;

public class MinesweeperSquareComponent implements Component {
  public static final int SQUARE_SIZE = 20;
  private final Integer adjacentCount;
  private boolean isRevealed;
  private boolean isFlagged;

  public MinesweeperSquareComponent(Integer adjacentCount) {
    this.adjacentCount = adjacentCount;
  }

  public Integer getAdjacentCount() {
    return adjacentCount;
  }

  public boolean isBomb() {
    return adjacentCount == null;
  }

  public boolean isRevealed() {
    return isRevealed;
  }

  public boolean isFlagged() {
    return isFlagged;
  }

  public void reveal() {
    isRevealed = true;
  }

  public void toggleFlag() {
    isFlagged = !isFlagged;
  }
}
