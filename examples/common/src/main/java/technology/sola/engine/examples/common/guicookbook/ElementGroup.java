package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

import java.util.Arrays;

public class ElementGroup {
  protected final SolaGuiDocument document;
  private final String title;
  private final ElementDemo[] elementDemos;

  public ElementGroup(SolaGuiDocument document, String title, ElementDemo... elementDemos) {
    this.document = document;
    this.title = title;
    this.elementDemos = elementDemos;
  }

  public String getTitle() {
    return title;
  }

  public GuiElement<?> build() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10).setId("category"),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText(title).margin.setLeft(20).margin.setRight(10)
        ),
        document.createElement(
          StreamGuiElementContainer::new,
          p -> p.setGap(5),
          Arrays.stream(elementDemos).map(this::makeNavButton).toList().toArray(new GuiElement<?>[]{})
        )
      )
    );
  }

  private GuiElement<?> makeNavButton(ElementDemo elementDemo) {
    return document.createElement(
      ButtonGuiElement::new,
      p -> p.setText(elementDemo.getTitle())
    ).setOnAction(() -> {
      var rootEle = document.getElementById("category", StreamGuiElementContainer.class);

      rootEle.removeChild(document.getElementById("element"));
      rootEle.addChild(elementDemo.build());
    });
  }
}
