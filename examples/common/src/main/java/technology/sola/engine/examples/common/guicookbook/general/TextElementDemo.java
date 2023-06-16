package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public class TextElementDemo extends ElementDemo {
  public TextElementDemo(SolaGuiDocument document) {
    super(document, "Text");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Colors")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Red").setColorText(Color.RED)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Green").setColorText(Color.GREEN)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Blue").setColorText(Color.BLUE)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Orange on hover").hover.setColorText(Color.ORANGE)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10).setWidth(202).setBorderColor(Color.DARK_GRAY),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Text align")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Left").setTextAlign(BaseTextGuiElement.TextAlign.LEFT).setWidth(200)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Center").setTextAlign(BaseTextGuiElement.TextAlign.CENTER).setWidth(200)
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Right").setTextAlign(BaseTextGuiElement.TextAlign.RIGHT).setWidth(200)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Fonts")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("default (monospaced_NORMAL_16)")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("arial_NORMAL_16").setFontAssetId("arial_NORMAL_16")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("times_NORMAL_18").setFontAssetId("times_NORMAL_18")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10).setBorderColor(Color.DARK_GRAY),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Text wrapping")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("This is a longer text that will wrap into another line for sure.").setWidth(200)
        )
      )
    );
  }
}
