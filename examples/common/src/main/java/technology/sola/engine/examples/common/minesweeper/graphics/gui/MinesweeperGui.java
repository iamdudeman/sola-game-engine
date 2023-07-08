package technology.sola.engine.examples.common.minesweeper.graphics.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public class MinesweeperGui {
  public static GuiElement<?> build(SolaGuiDocument document, EventHub eventHub) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(5).setBackgroundColor(Color.WHITE).padding.set(10),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("sola Minesweeper")
      ),
      document.createElement(
        ButtonGuiElement::new,
        p -> p.setText("New game").padding.set(5)
      ).setOnAction(() -> {
        eventHub.emit(new NewGameEvent(30, 40, 16));
      })
    );
  }
}
