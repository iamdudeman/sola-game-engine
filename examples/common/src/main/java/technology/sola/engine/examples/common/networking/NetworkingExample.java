package technology.sola.engine.examples.common.networking;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
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
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.input.Key;
import technology.sola.engine.networking.socket.SocketMessage;

import java.util.Date;

/**
 * NetworkingExample is a {@link technology.sola.engine.core.Sola} that demos a simple socket based game. This requires
 * that examples:server:ServerMain is running.
 */
public class NetworkingExample extends SolaWithDefaults {
  private long clientPlayerId = -1;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public NetworkingExample() {
    super(SolaConfiguration.build("Networking Example", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useGraphics().useDebug();

    solaEcs.setWorld(new World(50));
    solaEcs.addSystems(new NetworkQueueSystem(), new PlayerSystem());
    solaGuiDocument.setGuiRoot(buildGui());

    ButtonGuiElement updateTimeButton = solaGuiDocument.getElementById("updateTime", ButtonGuiElement.class);
    ButtonGuiElement connectButton = solaGuiDocument.getElementById("connect", ButtonGuiElement.class);
    ButtonGuiElement disconnectButton = solaGuiDocument.getElementById("disconnect", ButtonGuiElement.class);
    connectButton.setOnAction(() -> {
      platform.getSocketClient().connect("127.0.0.1", 1380);
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

  private class PlayerSystem extends EcsSystem {
    private static final int MOVEMENT = 10;

    @Override
    public void update(World world, float deltaTime) {
      Entity clientPlayerEntity = world.findEntityByUniqueId(String.valueOf(clientPlayerId));

      if (clientPlayerEntity != null) {
        TransformComponent transformComponent = clientPlayerEntity.getComponent(TransformComponent.class);

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
      }
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

            solaGuiDocument.getElementById("time", TextGuiElement.class).properties()
              .setText(new Date(updateTimeMessage.getTime()).toString());
          }
          case ASSIGN_PLAYER_ID -> {
            AssignPlayerIdMessage assignPlayerIdMessage = AssignPlayerIdMessage.parse(socketMessage);

            clientPlayerId = assignPlayerIdMessage.getClientPlayerId();
          }
          case PLAYER_ADDED -> {
            PlayerAddedMessage playerAddedMessage = PlayerAddedMessage.parse(socketMessage);

            solaEcs.getWorld().createEntity(
              String.valueOf(playerAddedMessage.getClientPlayerId()), "player-" + playerAddedMessage.getClientPlayerId(),
              new TransformComponent(400, 400, 25),
              new CircleRendererComponent(Color.WHITE, true)
            );
          }
          case PLAYER_REMOVED -> {
            PlayerRemovedMessage playerRemovedMessage = PlayerRemovedMessage.parse(socketMessage);
            Entity playerEntity = solaEcs.getWorld().findEntityByUniqueId(String.valueOf(playerRemovedMessage.getClientPlayerId()));

            if (playerEntity != null) {
              playerEntity.destroy();
            }
          }
          case PLAYER_UPDATE -> {
            PlayerUpdateMessage playerUpdateMessage = PlayerUpdateMessage.parse(socketMessage);
            String playerId = String.valueOf(playerUpdateMessage.getClientPlayerId());
            Entity playerEntity = solaEcs.getWorld().findEntityByUniqueId(playerId);

            if (playerEntity == null) {
              solaEcs.getWorld().createEntity(
                playerId, "player-" + playerId,
                new TransformComponent(playerUpdateMessage.getPosition().x(), playerUpdateMessage.getPosition().y(), 25),
                new CircleRendererComponent(Color.WHITE, true)
              );
            } else {
              playerEntity.getComponent(TransformComponent.class).setTranslate(playerUpdateMessage.getPosition());
            }
          }
        }
      }
    }
  }

  private GuiElement<?> buildGui() {
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(5).padding.set(5),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("Networking Example")
      ),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("").setId("time")
      ),
      solaGuiDocument.createElement(
        ButtonGuiElement::new,
        p -> p.setText("Connect").setId("connect").setWidth(200).padding.set(15)
      ),
      solaGuiDocument.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Update Time").setId("updateTime").setWidth(200).padding.set(15)
      ),
      solaGuiDocument.createElement(
        ButtonGuiElement::new,
        p -> p.setDisabled(true).setText("Disconnect").setId("disconnect").setWidth(200).padding.set(15)
      )
    );
  }
}
