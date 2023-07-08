package technology.sola.engine.examples.common.minesweeper.graphics.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.event.FlagEvent;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public class MinesweeperGui {
  public static final Size[] sizeOptions = new Size[] {
    new Size(10, 10),
    new Size(15, 15),
    new Size(20, 20),
    new Size(25, 30),
    new Size(30, 40)
  };
  public static final int[] difficultyOptions = new int[] {
    8,
    12,
    16,
    20
  };
  private static int mineCount = 0;
  private static int sizeIndex = 0;
  private static int difficultyIndex = 0;

  public static GuiElement<?> build(SolaGuiDocument document, EventHub eventHub) {
    eventHub.add(FlagEvent.class, flagEvent -> {
      mineCount += flagEvent.isAddingFlag() ? -1 : 1;
      document.getElementById("title", TextGuiElement.class).properties().setText("sola Minesweeper - " + mineCount);
    });

    eventHub.add(NewGameEvent.class, newGameEvent -> {
      mineCount = newGameEvent.totalMines();
      document.getElementById("title", TextGuiElement.class).properties().setText("sola Minesweeper - " + mineCount);
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(120).setBackgroundColor(Color.WHITE).padding.set(5),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("sola Minesweeper").setId("title")
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("New game").padding.set(5)
        ).setOnAction(() -> {
          newGame(eventHub);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Change size").padding.set(5)
        ).setOnAction(() -> {
          sizeIndex = (sizeIndex + 1) % sizeOptions.length;
          newGame(eventHub);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Change difficulty").padding.set(5)
        ).setOnAction(() -> {
          difficultyIndex = (difficultyIndex + 1) % difficultyOptions.length;
          newGame(eventHub);
        })
      )
    );
  }

  private static void newGame(EventHub eventHub) {
    Size size = sizeOptions[sizeIndex];
    int difficulty = difficultyOptions[difficultyIndex];

    eventHub.emit(new NewGameEvent(size.rows, size.columns, difficulty));
  }

  public record Size(int rows, int columns) {
  }
}
