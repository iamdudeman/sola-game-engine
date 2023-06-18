package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

// todo
//  background + hover
//  padding
//  margin

public class CommonPropertiesDemo extends ElementDemo {
  public CommonPropertiesDemo(SolaGuiDocument document) {
    super(document, "Common Properties");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(10).setDirection(StreamGuiElementContainer.Direction.VERTICAL),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Borders")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Green w/ orange hover").setBorderColor(Color.GREEN).hover.setBorderColor(Color.ORANGE)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Red w/ blue hover").setBorderColor(Color.RED).hover.setBorderColor(Color.BLUE)
        )
      )
    );
  }
}
