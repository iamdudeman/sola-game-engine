package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementPage;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public class TextPage extends ElementPage {
  public TextPage(SolaGuiDocument document) {
    super(document, "Text");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(5),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("Text")
      )
    );
  }
}
