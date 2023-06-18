package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public class ImageElementDemo extends ElementDemo {
  public ImageElementDemo(SolaGuiDocument document) {
    super(document, "Image");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),

      createExampleContainer("Original size",
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles")
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("duck")
        )
      ),

      createExampleContainer("50x50 size",
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles").setWidth(50).setHeight(50)
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("duck").setWidth(50).setHeight(50)
        )
      ),

      createExampleContainer("200x200 size",
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("test_tiles").setWidth(200).setHeight(200)
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("duck").setWidth(200).setHeight(200)
        )
      )
    );
  }
}
