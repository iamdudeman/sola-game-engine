package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

/**
 * {@link ElementDemo} for {@link technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties} common
 * across {@link GuiElement}.
 */
public class CommonPropertiesDemo extends ElementDemo {
  /**
   * Creates a new instance.
   *
   * @param document the {@link SolaGuiDocument}
   */
  public CommonPropertiesDemo(SolaGuiDocument document) {
    super(document, "Common Properties");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),
      createExampleContainer("Borders",
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Green w/ orange hover").setBorderColor(Color.GREEN).hover.setBorderColor(Color.ORANGE)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Red w/ blue hover").setBorderColor(Color.RED).hover.setBorderColor(Color.BLUE)
        )
      ),

      createExampleContainer("Background",
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Green w/ orange hover").setBackgroundColor(Color.GREEN).hover.setBackgroundColor(Color.ORANGE)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Red w/ blue hover").setBackgroundColor(Color.RED).hover.setBackgroundColor(Color.BLUE)
        )
      ),

      createExampleContainer("Padding",
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("0").padding.set(0).setBorderColor(Color.BLACK)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("25,50").padding.set(25, 50).setBorderColor(Color.BLACK)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("50").padding.set(50).setBorderColor(Color.BLACK)
        )
      ),

      createExampleContainer("Margin",
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("0").margin.set(0).setBorderColor(Color.BLACK)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("25,50").margin.set(25, 50).setBorderColor(Color.BLACK)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("50").margin.set(50).setBorderColor(Color.BLACK)
        )
      )
    );
  }
}
