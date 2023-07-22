package technology.sola.engine.examples.common.guicookbook.container;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

/**
 * {@link ElementDemo} for the {@link StreamGuiElementContainer}.
 */
public class StreamContainerDemo extends ElementDemo {
  /**
   * Creates a new instance.
   *
   * @param document the {@link SolaGuiDocument}
   */
  public StreamContainerDemo(SolaGuiDocument document) {
    super(document, "Stream");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(25),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(10),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Increase gap")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setGap(Math.min(props.getGap() + 5, 50));
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Decrease gap")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setGap(Math.max(props.getGap() - 5, 0));
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Change direction")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setDirection(props.getDirection() == StreamGuiElementContainer.Direction.HORIZONTAL ? StreamGuiElementContainer.Direction.VERTICAL : StreamGuiElementContainer.Direction.HORIZONTAL);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Align start")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.LEFT);
          props.setVerticalAlignment(StreamGuiElementContainer.VerticalAlignment.TOP);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Align center")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.CENTER);
          props.setVerticalAlignment(StreamGuiElementContainer.VerticalAlignment.CENTER);
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Align end")
        ).setOnAction(() -> {
          var props = document.getElementById("streamDemo", StreamGuiElementContainer.class).properties();

          props.setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.RIGHT);
          props.setVerticalAlignment(StreamGuiElementContainer.VerticalAlignment.BOTTOM);
        })
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setId("streamDemo").setBorderColor(Color.BLACK).setWidth(1100).setHeight(600).padding.set(2),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("First")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Second")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Third")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Fourth")
        ),
        document.createElement(
          ImageGuiElement::new,
          p -> p.setAssetId("duck")
        ),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Do nothing button")
        ),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Do nothing button 2")
        )
      )
    );
  }
}
