package technology.sola.engine.examples.common.minesweeper.graphics.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public class MinesweeperGui {

  public static GuiElement<?> build(SolaGuiDocument document, EventHub eventHub) {
    return document.createElement(
      ButtonGuiElement::new,
      p -> p.setText("New game")
    ).setOnAction(() -> {
      eventHub.emit(new NewGameEvent(30, 40, 16));
    });
  }
}
