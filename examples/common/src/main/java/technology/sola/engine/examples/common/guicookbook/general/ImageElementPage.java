package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementPage;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public class ImageElementPage extends ElementPage {
  public ImageElementPage(SolaGuiDocument document) {
    super(document, "Image");
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
          p -> p.setText("Original size")
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Smaller size")
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles").setWidth(50).setHeight(50)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Larger size")
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles").setWidth(200).setHeight(200)
        )
      )
    );
  }
}
