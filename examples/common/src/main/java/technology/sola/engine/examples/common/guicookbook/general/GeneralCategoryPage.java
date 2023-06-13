package technology.sola.engine.examples.common.guicookbook.general;

import technology.sola.engine.examples.common.guicookbook.CategoryPage;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;

public class GeneralCategoryPage extends CategoryPage {
  public GeneralCategoryPage(SolaGuiDocument document) {
    super(document, "General");
  }

  @Override
  protected GuiElement<?>[] getElementNavigationOptions() {
    return new GuiElement[] {
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("test")
      )
    };
  }
}
