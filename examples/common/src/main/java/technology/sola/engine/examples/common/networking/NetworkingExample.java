package technology.sola.engine.examples.common.networking;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.UUID;

public class NetworkingExample extends Sola {
  private SolaGui solaGui;
  private final String userName;

  public NetworkingExample() {
    super(SolaConfiguration.build("Networking Example", 800, 600).withTargetUpdatesPerSecond(30));

    userName = UUID.randomUUID().toString();
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.useModule(assetLoaderProvider, platform, eventHub);

    solaGui.setGuiRoot(buildGui());

    ButtonGuiElement greetingButton = solaGui.getElementById("greeting", ButtonGuiElement.class);
    ButtonGuiElement connectButton = solaGui.getElementById("connect", ButtonGuiElement.class);
    ButtonGuiElement disconnectButton = solaGui.getElementById("disconnect", ButtonGuiElement.class);
    connectButton.setOnAction(() -> {
      platform.getSocketClient().connect("127.0.0.1", 60000);
      connectButton.properties().setDisabled(true);
      disconnectButton.properties().setDisabled(false);
      greetingButton.properties().setDisabled(false);
    });
    disconnectButton.setOnAction(() -> {
      platform.getSocketClient().disconnect();
      connectButton.properties().setDisabled(false);
      disconnectButton.properties().setDisabled(true);
      greetingButton.properties().setDisabled(true);
    });
    greetingButton.setOnAction(() -> {
      platform.getSocketClient().sendMessage(new ChatSocketMessage(userName, "Greetings!"));
    });
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render();
  }

  private GuiElement<?> buildGui() {
    return solaGui.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(5).padding.set(5),
      solaGui.createElement(
        TextGuiElement::new,
        p -> p.setText("Networking Example").setColorText(Color.WHITE)
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setText("Connect").setId("connect").setWidth(200).padding.set(15)
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Send Greeting").setId("greeting").setWidth(200).padding.set(15)
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Disconnect").setId("disconnect").setWidth(200).padding.set(15)
      )
    );
  }
}
