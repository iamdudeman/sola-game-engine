package technology.sola.engine.examples.common.networking;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.module.graphics.SolaGraphics;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.examples.common.networking.messages.AssignPlayerIdMessage;
import technology.sola.engine.examples.common.networking.messages.MessageType;
import technology.sola.engine.examples.common.networking.messages.PlayerAddedMessage;
import technology.sola.engine.examples.common.networking.messages.PlayerRemovedMessage;
import technology.sola.engine.examples.common.networking.messages.PlayerUpdateMessage;
import technology.sola.engine.examples.common.networking.messages.RequestTimeMessage;
import technology.sola.engine.examples.common.networking.messages.UpdateTimeMessage;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.networking.socket.SocketMessage;

import java.util.Date;

public class NetworkingExample extends Sola {
  private SolaGui solaGui;
  private SolaGraphics solaGraphics;
  private long clientPlayerId = -1;

  public NetworkingExample() {
    super(SolaConfiguration.build("Networking Example", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit() {
    solaGraphics = SolaGraphics.useModule(solaEcs, platform.getRenderer(), assetLoaderProvider);
    solaGraphics.setRenderDebug(true);

    solaEcs.setWorld(new World(50));
    solaEcs.addSystems(new NetworkQueueSystem(), new PlayerSystem());

    solaGui = SolaGui.useModule(assetLoaderProvider, platform, eventHub);
    solaGui.setGuiRoot(buildGui());

    ButtonGuiElement updateTimeButton = solaGui.getElementById("updateTime", ButtonGuiElement.class);
    ButtonGuiElement connectButton = solaGui.getElementById("connect", ButtonGuiElement.class);
    ButtonGuiElement disconnectButton = solaGui.getElementById("disconnect", ButtonGuiElement.class);
    connectButton.setOnAction(() -> {
      platform.getSocketClient().connect("127.0.0.1", 60000);
      connectButton.properties().setDisabled(true);
      disconnectButton.properties().setDisabled(false);
      updateTimeButton.properties().setDisabled(false);
    });
    disconnectButton.setOnAction(() -> {
      platform.getSocketClient().disconnect();
      connectButton.properties().setDisabled(false);
      disconnectButton.properties().setDisabled(true);
      updateTimeButton.properties().setDisabled(true);
      solaEcs.setWorld(new World(50));
    });
    updateTimeButton.setOnAction(() -> {
      platform.getSocketClient().sendMessage(new RequestTimeMessage());
    });
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();

    solaGui.render();
  }

  private class PlayerSystem extends EcsSystem {
    private static final int MOVEMENT = 10;

    @Override
    public void update(World world, float deltaTime) {
      world.findEntityByUniqueId("" + clientPlayerId).ifPresent(entity -> {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

        if (keyboardInput.isKeyHeld(Key.W)) {
          transformComponent.setY(transformComponent.getY() - MOVEMENT);
        }
        if (keyboardInput.isKeyHeld(Key.S)) {
          transformComponent.setY(transformComponent.getY() + MOVEMENT);
        }
        if (keyboardInput.isKeyHeld(Key.A)) {
          transformComponent.setX(transformComponent.getX() - MOVEMENT);
        }
        if (keyboardInput.isKeyHeld(Key.D)) {
          transformComponent.setX(transformComponent.getX() + MOVEMENT);
        }

        if (platform.getSocketClient().isConnected()) {
          platform.getSocketClient().sendMessage(new PlayerUpdateMessage(clientPlayerId, transformComponent.getTranslate()));
        }
      });
    }
  }

  private class NetworkQueueSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      while (!platform.getSocketClient().getNetworkQueue().isEmpty()) {
        SocketMessage socketMessage = platform.getSocketClient().getNetworkQueue().removeFirst();
        MessageType messageType = MessageType.values()[socketMessage.getType()];

        switch (messageType) {
          case UPDATE_TIME -> {
            UpdateTimeMessage updateTimeMessage = UpdateTimeMessage.parse(socketMessage);

            solaGui.getElementById("time", TextGuiElement.class).properties()
              .setText(new Date(updateTimeMessage.getTime()).toString());
          }
          case ASSIGN_PLAYER_ID -> {
            AssignPlayerIdMessage assignPlayerIdMessage = AssignPlayerIdMessage.parse(socketMessage);

            clientPlayerId = assignPlayerIdMessage.getClientPlayerId();
          }
          case PLAYER_ADDED -> {
            PlayerAddedMessage playerAddedMessage = PlayerAddedMessage.parse(socketMessage);

            solaEcs.getWorld().createEntity(
              "" + playerAddedMessage.getClientPlayerId(), "player-" + playerAddedMessage.getClientPlayerId(),
              new TransformComponent(400, 400, 25),
              new CircleRendererComponent(Color.WHITE, true)
            );
          }
          case PLAYER_REMOVED -> {
            PlayerRemovedMessage playerRemovedMessage = PlayerRemovedMessage.parse(socketMessage);

            solaEcs.getWorld().findEntityByUniqueId("" + playerRemovedMessage.getClientPlayerId())
              .ifPresent(Entity::destroy);
          }
          case PLAYER_UPDATE -> {
            PlayerUpdateMessage playerUpdateMessage = PlayerUpdateMessage.parse(socketMessage);

            solaEcs.getWorld().findEntityByUniqueId("" + playerUpdateMessage.getClientPlayerId())
              .ifPresentOrElse(entity ->
                  entity.getComponent(TransformComponent.class).setTranslate(playerUpdateMessage.getPosition()),
                () -> solaEcs.getWorld().createEntity(
                  "" + playerUpdateMessage.getClientPlayerId(), "player-" + playerUpdateMessage.getClientPlayerId(),
                  new TransformComponent(playerUpdateMessage.getPosition().x(), playerUpdateMessage.getPosition().y(), 25),
                  new CircleRendererComponent(Color.WHITE, true)
                ));
          }
        }
      }
    }
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
        TextGuiElement::new,
        p -> p.setText("").setColorText(Color.WHITE).setId("time")
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setText("Connect").setId("connect").setWidth(200).padding.set(15)
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Update Time").setId("updateTime").setWidth(200).padding.set(15)
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Disconnect").setId("disconnect").setWidth(200).padding.set(15)
      )
    );
  }
}
