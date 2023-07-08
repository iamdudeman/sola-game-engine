package technology.sola.engine.examples.common.minesweeper.graphics.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.event.FlagEvent;
import technology.sola.engine.examples.common.minesweeper.event.GameOverEvent;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public class MinesweeperGui {
  public static final Size[] SIZE_OPTIONS = new Size[] {
    new Size(10, 10),
    new Size(15, 15),
    new Size(20, 20),
    new Size(25, 30),
    new Size(30, 40),
  };
  public static final int[] DIFFICULTY_OPTIONS = new int[] {
    8,
    12,
    16,
    20,
  };
  private static int mineCount = 0;
  private static int sizeIndex = 0;
  private static int difficultyIndex = 0;
  private static long timeStarted;

  public static GuiElement<?> build(SolaGuiDocument document, EventHub eventHub) {
    eventHub.add(FlagEvent.class, flagEvent -> {
      mineCount += flagEvent.isAddingFlag() ? -1 : 1;
      document.getElementById("title", TextGuiElement.class).properties().setText("sola Minesweeper - " + mineCount);
    });

    eventHub.add(NewGameEvent.class, newGameEvent -> {
      mineCount = newGameEvent.totalMines();
      document.getElementById("title", TextGuiElement.class).properties().setText("sola Minesweeper - " + mineCount);
      document.getElementById("victory", TextGuiElement.class).properties().setHidden(true);
      timeStarted = System.currentTimeMillis();
    });

    eventHub.add(GameOverEvent.class, gameOverEvent -> {
      if (gameOverEvent.isVictory()) {
        long millisTaken = System.currentTimeMillis() - timeStarted;
        double secondsTaken = millisTaken / 1000f;

        document.getElementById("victory", TextGuiElement.class).properties()
          .setText(String.format("You won in %.2f seconds", secondsTaken))
          .setHidden(false);
      }
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(20).setBackgroundColor(Color.WHITE).padding.set(5),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(3).setDirection(StreamGuiElementContainer.Direction.VERTICAL),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("sola Minesweeper").setId("title")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("You won in 0 seconds").setId("victory").setHidden(true)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("New game").padding.set(5)
        ).setOnAction(() -> newGame(eventHub)),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Change size").padding.set(5)
        ).setOnAction(() -> {
          sizeIndex = (sizeIndex + 1) % SIZE_OPTIONS.length;
          newGame(eventHub);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Change difficulty").padding.set(5)
        ).setOnAction(() -> {
          difficultyIndex = (difficultyIndex + 1) % DIFFICULTY_OPTIONS.length;
          newGame(eventHub);
        })
      )
    );
  }

  private static void newGame(EventHub eventHub) {
    Size size = SIZE_OPTIONS[sizeIndex];
    int difficulty = DIFFICULTY_OPTIONS[difficultyIndex];

    eventHub.emit(new NewGameEvent(size.rows, size.columns, difficulty));
  }

  public record Size(int rows, int columns) {
  }
}
