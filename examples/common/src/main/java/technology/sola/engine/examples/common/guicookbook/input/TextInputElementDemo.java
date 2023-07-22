package technology.sola.engine.examples.common.guicookbook.input;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputGuiElement;

/**
 * {@link ElementDemo} for the {@link TextInputGuiElement}.
 */
public class TextInputElementDemo extends ElementDemo {
  /**
   * Creates a new instance.
   *
   * @param document the {@link SolaGuiDocument}
   */
  public TextInputElementDemo(SolaGuiDocument document) {
    super(document, "TextInput");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),
      createExampleContainer("Placeholder",
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setPlaceholder("Placeholder text")
        ),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setPlaceholder("Red placeholder").setColorPlaceholderText(Color.RED)
        )
      ),
      createExampleContainer("Max length",
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setMaxLength(5).setPlaceholder("5")
        ),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setMaxLength(10).setPlaceholder("10")
        ),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setMaxLength(15).setPlaceholder("15")
        )
      ),
      createExampleContainer("Disabled",
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Toggle disabled example state")
        ).setOnAction(() -> {
          var buttonProps1 = document.getElementById("disabled-one", TextInputGuiElement.class).properties();
          var buttonProps2 = document.getElementById("disabled-two", TextInputGuiElement.class).properties();
          var buttonProps3 = document.getElementById("disabled-three", TextInputGuiElement.class).properties();

          buttonProps1.setDisabled(!buttonProps1.isDisabled());
          buttonProps2.setDisabled(!buttonProps2.isDisabled());
          buttonProps3.setDisabled(!buttonProps3.isDisabled());
        }),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setPlaceholder("Placeholder text").setValue("Disabled").setDisabled(true).setId("disabled-one")
        ),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setPlaceholder("Disabled placeholder").setDisabled(true).setId("disabled-three")
        ),
        document.createElement(
          TextInputGuiElement::new,
          p -> p.setValue("Red disabled background").setPlaceholder("Placeholder text").setDisabled(true).setDisabledBackgroundColor(Color.RED).setId("disabled-two")
        )
      )
    );
  }
}
