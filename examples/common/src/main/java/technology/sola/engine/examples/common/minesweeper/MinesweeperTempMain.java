package technology.sola.engine.examples.common.minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperTempMain {
  public static void main(String[] args) {
    int[][] testField = generateMinefield(25, 20, 16);

    for (int[] row : testField) {
      System.out.println("length " + row.length);

      for (int tile : row) {
        //if point is bomb print '#'
        if (tile == -1) {
          System.out.print("#");
        } else if (tile == 0) { //if point is nothing then print " "
          System.out.print(" ");
        } else { //else print its number
          System.out.print(tile);
        }
        //space them out
        System.out.print(" ");
      }
      System.out.println();
    }
  }


  /**
   * Generates a field of mines for Minesweeper. Mines are represented as the int -1.
   *
   * @param rows         The number of rows in the field
   * @param columns      The number of columns in the field
   * @param percentMines The percentage of mines as calculated by (percentMines/100.0)*(rows*columns)
   * @return The mine field
   */
  public static int[][] generateMinefield(int rows, int columns, int percentMines) {
    int[][] minefield = new int[rows][columns];
    //calculate how many of mines to place
    int count = (int) (percentMines / 100.0 * (rows * columns));

    //place mines
    while (count > 0) {
      //place count number of mines
      if (placeMine(minefield)) {
        count--;
      }
    }

    //tally up counts
    for (int i = 0; i < minefield.length; i++) {
      for (int j = 0; j < minefield[i].length; j++) {
        //if current position is a mine
        if (minefield[i][j] == -1) {
          //get coordinates around position
          List<Point> points = getPointsAroundCoord(minefield, i, j);
          //for every point
          for (int k = 0; k < points.size(); k++) {
            Point temp = points.get(k);
            //check to see if point is not a mine
            if (minefield[temp.x][temp.y] != -1) {
              //if it is not a mine then increase its value by one(since it is by a mine)
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
    //random generator
    Random rand = new Random();
    //int from 0 to number of rows
    int x = rand.nextInt(minefield.length);
    //int from 0 to number of columns
    int y = rand.nextInt(minefield[0].length);
    //if minefield position is not a bomb
    if (minefield[x][y] != -1) {
      //get coordinates around point attempting to place a mine
      List<Point> points = getPointsAroundCoord(minefield, x, y);
      int count = 0;
      //while it has not been placed and we still have points around attempted mine place
      while (!isPlaced && count < points.size()) {
        Point temp = points.get(count);
        //if checked position is not a mine (this prevents a mine being surrounded by mines)
        if (minefield[temp.x][temp.y] != -1) {
          //place mine
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
   * @param array
   * @param x
   * @param y
   * @return
   */
  private static boolean isCoordIn2DArray(int[][] array, int x, int y) {
    boolean isInArray = false;
    //checks if x is greater than or equal to 0 and less than number of rows
    if (x >= 0 && x < array.length) {
      //checks if y is greater than or equal to 0 and less than numbef of columns
      if (y >= 0 && y < array[0].length) {
        isInArray = true;
      }
    }
    return isInArray;
  }

  /**
   * Gets all the valid points around a given coordinate
   *
   * @param array The array in which the coordinate is located
   * @param x     The x coord
   * @param y     The y coord
   * @return The List of Points
   */
  private static List<Point> getPointsAroundCoord(int[][] array, int x, int y) {
    List<Point> points = new ArrayList<>();
    if (isCoordIn2DArray(array, x, y)) {
      //top left corner of point
      int tempX = x - 1;
      int tempY = y - 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //top middle of point
      tempY = y;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //top right corner of point
      tempY = y + 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //left of point
      tempX = x;
      tempY = y - 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //right of point
      tempY = y + 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //bottom left corner of point
      tempX = x + 1;
      tempY = y - 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //bottom middle of point
      tempY = y;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
      //bottom right corner of point
      tempY = y + 1;
      if (isCoordIn2DArray(array, tempX, tempY)) {
        points.add(new Point(tempX, tempY));
      }
    }
    return points;
  }

  private record Point(int x, int y) {
  }
}
