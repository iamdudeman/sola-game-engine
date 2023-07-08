package technology.sola.engine.examples.common.minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperFieldGenerator {
  /**
   * Generates a field of mines for Minesweeper. Mines are represented as the int -1.
   *
   * @param rows         The number of rows in the field
   * @param columns      The number of columns in the field
   * @param percentMines The percentage of mines as calculated by (percentMines/100.0)*(rows*columns)
   * @return The minefield
   */
  public static int[][] generateMinefield(int rows, int columns, int percentMines) {
    int[][] minefield = new int[rows][columns];
    int count = (int) (percentMines / 100.0 * (rows * columns));

    while (count > 0) {
      if (placeMine(minefield)) {
        count--;
      }
    }

    // tally up counts
    for (int i = 0; i < minefield.length; i++) {
      for (int j = 0; j < minefield[i].length; j++) {
        // if current position is a mine
        if (minefield[i][j] == -1) {
          // get coordinates around position
          List<Point> points = getPointsAroundCoordinate(minefield, i, j);
          // for every point
          for (int k = 0; k < points.size(); k++) {
            Point temp = points.get(k);
            // check to see if point is not a mine
            if (minefield[temp.x][temp.y] != -1) {
              // if it is not a mine then increase its value by one(since it is by a mine)
              minefield[temp.x][temp.y]++;
            }
          }
        }
      }
    }
    return minefield;
  }

  /**
   * Attempts to place a mine on the field. Returns false if it was unsuccessful
   *
   * @param minefield The array to put a mine in
   * @return Success or fail
   */
  private static boolean placeMine(int[][] minefield) {
    boolean isPlaced = false;
    Random random = new Random();
    // int from 0 to number of rows
    int x = random.nextInt(minefield.length);
    // int from 0 to number of columns
    int y = random.nextInt(minefield[0].length);

    // if minefield position is not a bomb
    if (minefield[x][y] != -1) {
      // get coordinates around point attempting to place a mine
      List<Point> points = getPointsAroundCoordinate(minefield, x, y);
      int count = 0;

      // while it has not been placed and we still have points around attempted mine place
      while (!isPlaced && count < points.size()) {
        Point temp = points.get(count);

        // if checked position is not a mine (this prevents a mine being surrounded by mines)
        if (minefield[temp.x][temp.y] != -1) {
          // place mine
          minefield[x][y] = -1;
          isPlaced = true;
        }

        count++;
      }
    }
    return isPlaced;
  }

  /**
   * Checks to see if coordinate is in a 2d array
   *
   * @param array the 2d array
   * @param x     the x coordinate
   * @param y     the y coordinate
   * @return true if in the array
   */
  private static boolean isCoordinateIn2dArray(int[][] array, int x, int y) {
    boolean isInArray = false;

    // check rows
    if (x >= 0 && x < array.length) {
      // check columns
      if (y >= 0 && y < array[0].length) {
        isInArray = true;
      }
    }

    return isInArray;
  }

  /**
   * Gets all the valid points around a given coordinate
   *
   * @param array the array in which the coordinate is located
   * @param x     the x coordinate
   * @param y     the y coordinate
   * @return the List of Points
   */
  private static List<Point> getPointsAroundCoordinate(int[][] array, int x, int y) {
    List<Point> points = new ArrayList<>();
    if (isCoordinateIn2dArray(array, x, y)) {
      // top left corner of point
      int tempX = x - 1;
      int tempY = y - 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // top middle of point
      tempY = y;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // top right corner of point
      tempY = y + 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // left of point
      tempX = x;
      tempY = y - 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // right of point
      tempY = y + 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // bottom left corner of point
      tempX = x + 1;
      tempY = y - 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // bottom middle of point
      tempY = y;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }

      // bottom right corner of point
      tempY = y + 1;
      if (isCoordinateIn2dArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
    }

    return points;
  }

  private record Point(int x, int y) {
  }
}
