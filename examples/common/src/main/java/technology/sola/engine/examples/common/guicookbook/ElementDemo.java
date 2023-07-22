package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

/**
 * ElementDemo is the base class for creating a cookbook demo for a {@link GuiElement}.
 */
public abstract class ElementDemo {
  /**
   * The {@link SolaGuiDocument}.
   */
  protected final SolaGuiDocument document;
  private final String title;

  /**
   * Creates the ElementDemo instance.
   *
   * @param document the {@link SolaGuiDocument}
   * @param title    the title of the element demo
   */
  public ElementDemo(SolaGuiDocument document, String title) {
    this.document = document;
    this.title = title;
  }

  /**
   * @return the title of the element demo
   */
  public String getTitle() {
    return title;
  }

  /**
   * Builds a {@link GuiElement} for the demo of this element utilizing the {@link ElementDemo#getElementPage()}.
   *
   * @return the demo page
   */
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

  /**
   * Builds and returns the content for the element demo page.
   *
   * @return the element demo page content
   */
  protected abstract GuiElement<?> getElementPage();

  /**
   * Creates a {@link GuiElement} with a common layout for a specific example for an element.
   *
   * @param title   the title of the property or functionality being demoed
   * @param entries the different examples for the property or functionality
   * @return the container of examples
   */
  protected GuiElement<?> createExampleContainer(String title, GuiElement<?>... entries) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText(title)
      )
    ).addChild(entries);
  }
}
