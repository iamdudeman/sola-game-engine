package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public abstract class ElementDemo {
  protected final SolaGuiDocument document;
  private final String title;

  public ElementDemo(SolaGuiDocument document, String title) {
    this.document = document;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public GuiElement<?> build() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(15).setId("element"),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText(title).margin.setLeft(30)
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        getElementPage()
      )
    );
  }

  protected abstract GuiElement<?> getElementPage();
}
