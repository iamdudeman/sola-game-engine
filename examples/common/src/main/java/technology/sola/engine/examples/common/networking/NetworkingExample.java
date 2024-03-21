package technology.sola.engine.examples.common.networking;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.networking.messages.*;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
import technology.sola.engine.input.Key;
import technology.sola.engine.networking.socket.SocketMessage;

import java.util.Date;
import java.util.List;

/**
 * NetworkingExample is a {@link technology.sola.engine.core.Sola} that demos a simple socket based game. This requires
 * that examples:server:ServerMain is running.
 */
public class NetworkingExample extends SolaWithDefaults {
  public static final int MAX_PLAYERS = 10;
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

    solaEcs.setWorld(LevelBuilder.createWorld(MAX_PLAYERS));
    solaEcs.addSystems(new NetworkQueueSystem(), new PlayerSystem());

    var rootElement = buildGui();

    GuiTheme.getDefaultDarkTheme()
      .applyToTree(rootElement);

    guiDocument.setRootElement(rootElement);
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
      while (!platform.getSocketClient().getNetworkQueue().isEmpty() && platform.getSocketClient().isConnected()) {
        SocketMessage socketMessage = platform.getSocketClient().getNetworkQueue().removeFirst();
        MessageType messageType = MessageType.values()[socketMessage.getType()];

        switch (messageType) {
          case UPDATE_TIME -> {
            UpdateTimeMessage updateTimeMessage = UpdateTimeMessage.parse(socketMessage);

            guiDocument.findElementById("updateTimeButton", ButtonGuiElement.class)
              .findElementsByType(TextGuiElement.class)
              .get(0)
              .setText(new Date(updateTimeMessage.getTime()).toString());
          }
          case ASSIGN_PLAYER_ID -> {
            AssignPlayerIdMessage assignPlayerIdMessage = AssignPlayerIdMessage.parse(socketMessage);

            clientPlayerId = assignPlayerIdMessage.getClientPlayerId();
          }
          case PLAYER_ADDED -> {
//            PlayerAddedMessage playerAddedMessage = PlayerAddedMessage.parse(socketMessage);
//
//            solaEcs.getWorld().createEntity(
//              String.valueOf(playerAddedMessage.getClientPlayerId()),
//              "player-" + playerAddedMessage.getClientPlayerId(),
//              new TransformComponent(400, 400, 25),
//              new CircleRendererComponent(Color.WHITE, true),
//              new PlayerComponent()
//            );
          }
          case PLAYER_REMOVED -> {
            PlayerRemovedMessage playerRemovedMessage = PlayerRemovedMessage.parse(socketMessage);
            Entity playerEntity = solaEcs.getWorld().findEntityByUniqueId(String.valueOf(playerRemovedMessage.getClientPlayerId()));

            if (playerEntity != null) {
              playerEntity.destroy();
            }
          }
          case PLAYER_POSITION_UPDATES -> {
            PlayerPositionUpdatesMessage playerPositionUpdatesMessage = PlayerPositionUpdatesMessage.parse(socketMessage);

            playerPositionUpdatesMessage.getPlayerPositions().forEach(playerPosition -> {
              System.out.println(playerPosition.id() + " " + playerPosition.translate());
              Entity entity = solaEcs.getWorld().findEntityByUniqueId(playerPosition.id());

              if (entity == null) {
                solaEcs.getWorld().createEntity(
                  String.valueOf(playerPosition.id()),
                  "player-" + playerPosition.id(),
                  new TransformComponent(playerPosition.translate().x(), playerPosition.translate().y(), 25),
                  new CircleRendererComponent(Color.WHITE, true),
                  new PlayerComponent()
                );
              } else {
                entity.getComponent(TransformComponent.class).setTranslate(playerPosition.translate());
              }
            });
          }
          case PLAYER_UPDATE -> {
//            PlayerUpdateMessage playerUpdateMessage = PlayerUpdateMessage.parse(socketMessage);
//            String playerId = String.valueOf(playerUpdateMessage.getClientPlayerId());
//            Entity playerEntity = solaEcs.getWorld().findEntityByUniqueId(playerId);
//
//            if (playerEntity == null) {
//              solaEcs.getWorld().createEntity(
//                playerId, "player-" + playerId,
//                new TransformComponent(playerUpdateMessage.getPosition().x(), playerUpdateMessage.getPosition().y(), 25),
//                new CircleRendererComponent(Color.WHITE, true)
//              );
//            } else {
//              playerEntity.getComponent(TransformComponent.class).setTranslate(playerUpdateMessage.getPosition());
//            }
          }
        }
      }
    }
  }

  private GuiElement<?> buildGui() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setGap(5)
        .setPadding(5)
        .build()
    )));

    sectionGuiElement.appendChildren(
      new TextGuiElement().setText("Networking Example"),
      new TextGuiElement().setText("").setId("time"),
      buildButton("connectButton", "Connect", () -> {
        platform.getSocketClient().connect("127.0.0.1", 1380);

        sectionGuiElement.findElementById("connectButton", ButtonGuiElement.class).setDisabled(true);
        sectionGuiElement.findElementById("disconnectButton", ButtonGuiElement.class).setDisabled(false);
        sectionGuiElement.findElementById("updateTimeButton", ButtonGuiElement.class).setDisabled(false);
      }, false),
      buildButton("updateTimeButton", "Update Time via Socket", () -> {
        platform.getSocketClient().sendMessage(new RequestTimeMessage());
      }, true),
      buildButton("updateTimeButtonRest", "Update Time via Rest", () -> {
        platform.getRestClient().get("http://localhost:1381/time", response -> {
          guiDocument.findElementById("updateTimeButtonRest", ButtonGuiElement.class)
            .findElementsByType(TextGuiElement.class)
            .get(0)
            .setText(new Date(response.body().asObject().getLong("time")).toString());
        });
      }, false),
      buildButton("disconnectButton", "Disconnect", () -> {
        platform.getSocketClient().disconnect();

        solaEcs.setWorld(LevelBuilder.createWorld(MAX_PLAYERS));

        sectionGuiElement.findElementById("connectButton", ButtonGuiElement.class).setDisabled(false);
        sectionGuiElement.findElementById("disconnectButton", ButtonGuiElement.class).setDisabled(true);
        sectionGuiElement.findElementById("updateTimeButton", ButtonGuiElement.class).setDisabled(true);
      }, true)
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildButton(String id, String text, Runnable onAction, boolean isDisabled) {
    ButtonGuiElement buttonGuiElement = new ButtonGuiElement();

    buttonGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setWidth(200)
        .setPadding(15)
        .build()
    )));

    buttonGuiElement.setId(id);
    buttonGuiElement.setDisabled(isDisabled);
    buttonGuiElement.setOnAction(onAction);

    buttonGuiElement.appendChildren(
      new TextGuiElement().setText(text)
    );

    return buttonGuiElement;
  }
}
