package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public abstract class CategoryPage {
  protected final SolaGuiDocument document;
  private final String title;

  public CategoryPage(SolaGuiDocument document, String title) {
    this.document = document;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public GuiElement<?> build() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(5).setId("category"),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText(title)
        ),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Close")
        ).setOnAction(() -> {
          document.getElementById("root", StreamGuiElementContainer.class).removeChild(
            document.getElementById("category")
          );
        })
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        getElementNavigationOptions()
      )
    );
  }

  protected abstract GuiElement<?>[] getElementNavigationOptions();
}
