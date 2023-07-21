package technology.sola.engine.examples.common.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {@link EcsSystem} for handling creating minefields when a new game is started.
 */
public class MinefieldSystem extends EcsSystem {
  private static final int MINE_INDICATOR = -1;
  private final SolaEcs solaEcs;
  private NewGameEvent newGameEvent;

  /**
   * Creates a MinefieldSystem instance that is currently inactive.
   *
   * @param solaEcs the {@link SolaEcs}
   */
  public MinefieldSystem(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
    setActive(false);
  }

  /**
   * Registers event listener to {@link NewGameEvent} that activates the system to create a new minefield.
   *
   * @param eventHub the {@link EventHub}
   */
  public void registerEvents(EventHub eventHub) {
    eventHub.add(NewGameEvent.class, newGameEvent -> {
      this.newGameEvent = newGameEvent;
      setActive(true);
    });
  }

  @Override
  public int getOrder() {
    return -999;
  }

  @Override
  public void update(World world, float deltaTime) {
    if (newGameEvent != null) {
      solaEcs.setWorld(buildWorld(newGameEvent.rows(), newGameEvent.columns(), newGameEvent.totalMines()));
      newGameEvent = null;
    }

    setActive(false);
  }

  private World buildWorld(int rows, int columns, int mines) {
    World world = new World(rows * columns);
    int[][] minefield = generateMinefield(rows, columns, mines);

    for (int rowIndex = 0; rowIndex < minefield.length; rowIndex++) {
      for (int columnIndex = 0; columnIndex < minefield[rowIndex].length; columnIndex++) {
        int tile = minefield[rowIndex][columnIndex];
        float x = columnIndex * MinesweeperSquareComponent.SQUARE_SIZE;
        float y = rowIndex * MinesweeperSquareComponent.SQUARE_SIZE + 60; // 60 is offset to prevent gui overlap

        world.createEntity(
          new TransformComponent(x, y),
          new MinesweeperSquareComponent(rowIndex, columnIndex, tile == MINE_INDICATOR ? null : tile)
        );
      }
    }

    return world;
  }


  /**
   * Generates a field of mines for Minesweeper. Mines are represented as the int -1.
   *
   * @param rows      the number of rows in the field
   * @param columns   the number of columns in the field
   * @param mineCount the total number of mines as calculated by (percentMines/100.0)*(rows*columns)
   * @return the minefield
   */
  private int[][] generateMinefield(int rows, int columns, int mineCount) {
    int[][] minefield = new int[rows][columns];
    int count = mineCount;

    while (count > 0) {
      if (placeMine(minefield)) {
        count--;
      }
    }

    // tally up counts
    for (int i = 0; i < minefield.length; i++) {
      for (int j = 0; j < minefield[i].length; j++) {
        // if current position is a mine
        if (minefield[i][j] == MINE_INDICATOR) {
          // get coordinates around position
          List<Point> points = getPointsAroundCoordinate(minefield, i, j);

          for (Point temp : points) {
            // check to see if point is not a mine
            if (minefield[temp.x][temp.y] != MINE_INDICATOR) {
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
  private boolean placeMine(int[][] minefield) {
    boolean isPlaced = false;
    Random random = new Random();
    // int from 0 to number of rows
    int x = random.nextInt(minefield.length);
    // int from 0 to number of columns
    int y = random.nextInt(minefield[0].length);

    // if minefield position is not a bomb
    if (minefield[x][y] != MINE_INDICATOR) {
      // get coordinates around point attempting to place a mine
      List<Point> points = getPointsAroundCoordinate(minefield, x, y);
      int count = 0;

      // while it has not been placed, and we still have points around attempted mine place
      while (!isPlaced && count < points.size()) {
        Point temp = points.get(count);

        // if checked position is not a mine (this prevents a mine being surrounded by mines)
        if (minefield[temp.x][temp.y] != MINE_INDICATOR) {
          // place mine
          minefield[x][y] = MINE_INDICATOR;
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
  private boolean isCoordinateIn2dArray(int[][] array, int x, int y) {
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
  private List<Point> getPointsAroundCoordinate(int[][] array, int x, int y) {
    List<Point> points = new ArrayList<>();

    if (isCoordinateIn2dArray(array, x, y)) {
      // top left corner of point
      int tempX = x - 1;
      int tempY = y - 1;
      addIfInArray(points, array, tempX, tempY);

      // top middle of point
      tempY = y;
      addIfInArray(points, array, tempX, tempY);

      // top right corner of point
      tempY = y + 1;
      addIfInArray(points, array, tempX, tempY);

      // left of point
      tempX = x;
      tempY = y - 1;
      addIfInArray(points, array, tempX, tempY);

      // right of point
      tempY = y + 1;
      addIfInArray(points, array, tempX, tempY);

      // bottom left corner of point
      tempX = x + 1;
      tempY = y - 1;
      addIfInArray(points, array, tempX, tempY);

      // bottom middle of point
      tempY = y;
      addIfInArray(points, array, tempX, tempY);

      // bottom right corner of point
      tempY = y + 1;
      addIfInArray(points, array, tempX, tempY);
    }

    return points;
  }

  private void addIfInArray(List<Point> points, int[][] array, int tempX, int tempY) {
    if (isCoordinateIn2dArray(array, tempX, tempY)) {
      points.add(new Point(tempX, tempY));
    }
  }

  private record Point(int x, int y) {
  }
}
