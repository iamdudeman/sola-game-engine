package technology.sola.engine.examples.common.games.minesweeper.graphics.gui;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.games.minesweeper.event.FlagEvent;
import technology.sola.engine.examples.common.games.minesweeper.event.GameOverEvent;
import technology.sola.engine.examples.common.games.minesweeper.event.NewGameEvent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Visibility;

import java.text.DecimalFormat;

/**
 * MinesweeperGui contains the functionality needed to build the Minesweeper GUI.
 */
@NullMarked
public class MinesweeperGui {
  /**
   * The valid minefield size options.
   */
  public static final Size[] SIZE_OPTIONS = new Size[]{
    new Size(10, 10),
    new Size(15, 15),
    new Size(20, 20),
    new Size(25, 30),
    new Size(30, 40),
  };
  /**
   * The valid difficulty options.
   */
  public static final int[] DIFFICULTY_OPTIONS = new int[]{
    8,
    12,
    16,
    20,
  };
  private static final ConditionalStyle<TextStyles> VISIBLE_STYLE = ConditionalStyle.always(
    new TextStyles.Builder<>().setVisibility(Visibility.VISIBLE).build()
  );
  private static int mineCount = 0;
  private static int sizeIndex = 0;
  private static int difficultyIndex = 0;
  private static long timeStarted;

  /**
   * Initialize event handlers for the various GUI elements for sola minesweeper
   *
   * @param rootElement the root {@link GuiElement}
   * @param eventHub    the {@link EventHub}
   */
  public static void initializeEvents(GuiElement<?, ?> rootElement, EventHub eventHub) {
    rootElement.findElementById("buttonNewGame", ButtonGuiElement.class).setOnAction(() -> newGame(eventHub));
    rootElement.findElementById("buttonSize", ButtonGuiElement.class).setOnAction(() -> {
      sizeIndex = (sizeIndex + 1) % SIZE_OPTIONS.length;
      newGame(eventHub);
    });
    rootElement.findElementById("buttonDifficulty", ButtonGuiElement.class).setOnAction(() -> {
      difficultyIndex = (difficultyIndex + 1) % DIFFICULTY_OPTIONS.length;
      newGame(eventHub);
    });

    eventHub.add(FlagEvent.class, flagEvent -> {
      mineCount += flagEvent.isAddingFlag() ? -1 : 1;
      rootElement.findElementById("title", TextGuiElement.class).setText("sola minesweeper - " + mineCount);
    });

    eventHub.add(NewGameEvent.class, newGameEvent -> {
      mineCount = newGameEvent.totalMines();
      rootElement.findElementById("title", TextGuiElement.class).setText("sola minesweeper - " + mineCount);
      rootElement.findElementById("victoryMessage", TextGuiElement.class).styles().removeStyle(VISIBLE_STYLE);
      rootElement.findElementById("textSize", TextGuiElement.class).setText(newGameEvent.rows() + "x" + newGameEvent.columns());

      String difficulty = switch (difficultyIndex) {
        case 0 -> "Easy";
        case 1 -> "Okay";
        case 2 -> "Hard";
        default -> "Whoa";
      };

      rootElement.findElementById("textDifficulty", TextGuiElement.class).setText(difficulty);
      timeStarted = System.currentTimeMillis();
    });

    eventHub.add(GameOverEvent.class, gameOverEvent -> {
      if (gameOverEvent.isVictory()) {
        long millisTaken = System.currentTimeMillis() - timeStarted;
        double secondsTaken = millisTaken / 1000f;
        DecimalFormat decimalFormat = new DecimalFormat("##.00");

        rootElement.findElementById("victoryMessage", TextGuiElement.class)
          .setText("You won in " + decimalFormat.format(secondsTaken) + " seconds")
          .styles().addStyle(VISIBLE_STYLE);
      }
    });
  }

  private static void newGame(EventHub eventHub) {
    Size size = SIZE_OPTIONS[sizeIndex];
    int difficulty = DIFFICULTY_OPTIONS[difficultyIndex];

    eventHub.emit(new NewGameEvent(size.rows, size.columns, difficulty));
  }

  /**
   * Size represents the size of a minefield in rows by columns.
   *
   * @param rows    the number of rows
   * @param columns the number of columns
   */
  public record Size(int rows, int columns) {
  }
}
