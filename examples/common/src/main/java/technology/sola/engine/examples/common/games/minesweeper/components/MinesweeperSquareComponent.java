package technology.sola.engine.examples.common.games.minesweeper.components;

import technology.sola.ecs.Component;

/**
 * {@link Component} that contains data for a minefield square.
 */
public class MinesweeperSquareComponent implements Component {
  /**
   * The size in pixels of a square.
   */
  public static final int SQUARE_SIZE = 20;
  private final int rowIndex;
  private final int columnIndex;
  private final Integer adjacentCount;
  private boolean isRevealed;
  private boolean isFlagged;

  /**
   * Creates a MinesweeperSquareComponent instance.
   *
   * @param rowIndex      the row the square is in
   * @param columnIndex   the column the square is in
   * @param adjacentCount the number of bombs adjacent to this square (or null if it is a bomb)
   */
  public MinesweeperSquareComponent(int rowIndex, int columnIndex, Integer adjacentCount) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
    this.adjacentCount = adjacentCount;
  }

  /**
   * @return the row
   */
  public int getRowIndex() {
    return rowIndex;
  }

  /**
   * @return the column
   */
  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * @return the adjacent bomb count or null if it is a bomb
   */
  public Integer getAdjacentCount() {
    return adjacentCount;
  }

  /**
   * @return true if the square is hiding a bomb
   */
  public boolean isBomb() {
    return adjacentCount == null;
  }

  /**
   * @return whether the square has been revealed or not
   */
  public boolean isRevealed() {
    return isRevealed;
  }

  /**
   * @return whether the square has been flagged as a potential bomb or not
   */
  public boolean isFlagged() {
    return isFlagged;
  }

  /**
   * Reveals the square.
   */
  public void reveal() {
    isRevealed = true;
  }

  /**
   * Toggles whether the square is flagged or not.
   */
  public void toggleFlag() {
    isFlagged = !isFlagged;
  }
}
