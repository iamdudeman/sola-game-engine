package technology.sola.engine.examples.common.guicookbook.input;

import technology.sola.engine.examples.common.guicookbook.ElementDemo;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

public class ButtonElementDemo extends ElementDemo {
  public ButtonElementDemo(SolaGuiDocument document) {
    super(document, "Button");
  }

  @Override
  protected GuiElement<?> getElementPage() {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(25),
      createExampleContainer("setOnAction",
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Off").setId("button")
        ).setOnAction(() -> {
          var buttonProps = document.getElementById("button", ButtonGuiElement.class).properties();

          buttonProps.setText(buttonProps.getText().equals("Off") ? "On" : "Off");
        }),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Toggle disabled example state")
        ).setOnAction(() -> {
          var buttonProps1 = document.getElementById("disabled-one", ButtonGuiElement.class).properties();
          var buttonProps2 = document.getElementById("disabled-two", ButtonGuiElement.class).properties();

          buttonProps1.setDisabled(!buttonProps1.isDisabled());
          buttonProps2.setDisabled(!buttonProps2.isDisabled());
        })
      ),

      createExampleContainer("Disabled",
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setDisabled(true).setText("Disabled button").setId("disabled-one")
        ),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setDisabled(true).setDisabledBackgroundColor(Color.RED).setText("Red disabled background").setId("disabled-two")
        )
      )
    );
  }
}
