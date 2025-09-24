package technology.sola.engine.examples.common.features.networking;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.features.networking.messages.*;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.networking.socket.SocketMessage;

import java.util.Date;

/**
 * NetworkingExample is a {@link technology.sola.engine.core.Sola} that demos a simple socket-based game. This requires
 * that <b>examples:server:ServerMain</b> is running.
 */
@NullMarked
public class NetworkingExample extends Sola {
  /**
   * The maximum number of players that can connect.
   */
  public static final int MAX_PLAYERS = 10;
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public NetworkingExample() {
    super(new SolaConfiguration("Networking Example", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .withDebug(null, eventHub, keyboardInput)
      .buildAndInitialize(assetLoaderProvider);

    solaEcs.setWorld(LevelBuilder.createWorld(MAX_PLAYERS));
    solaEcs.addSystems(new NetworkQueueSystem(), new PlayerSystem());

    var rootElement = buildGui();

    DefaultThemeBuilder.buildDarkTheme()
      .applyToTree(rootElement);

    solaGraphics.guiDocument().setRootElement(rootElement);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      int direction = 0;

      if (keyboardInput.isKeyHeld(Key.A)) {
        direction = 1;
      }
      if (keyboardInput.isKeyHeld(Key.D)) {
        direction = 2;
      }

      if (platform().getSocketClient().isConnected()) {
        platform().getSocketClient().sendMessage(new PlayerMoveMessage(direction));
      }
    }
  }

  private class NetworkQueueSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      while (!platform().getSocketClient().getNetworkQueue().isEmpty() && platform().getSocketClient().isConnected()) {
        SocketMessage socketMessage = platform().getSocketClient().getNetworkQueue().removeFirst();
        MessageType messageType = MessageType.values()[socketMessage.getType()];

        switch (messageType) {
          case UPDATE_TIME -> {
            UpdateTimeMessage updateTimeMessage = UpdateTimeMessage.parse(socketMessage);

            solaGraphics.guiDocument().findElementById("updateTimeButton", ButtonGuiElement.class)
              .findElementsByType(TextGuiElement.class)
              .get(0)
              .setText(new Date(updateTimeMessage.getTime()).toString());
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
        }
      }
    }
  }

  private GuiElement<?, ?> buildGui() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.addStyle(ConditionalStyle.always(
      new BaseStyles.Builder<>()
        .setGap(5)
        .setPadding(5)
        .build()
    ));

    sectionGuiElement.appendChildren(
      new TextGuiElement().setText("Networking Example"),
      new TextGuiElement().setText("").setId("time"),
      buildButton("connectButton", "Connect", () -> {
        platform().getSocketClient().connect("127.0.0.1", 1380);

        sectionGuiElement.findElementById("connectButton", ButtonGuiElement.class).setDisabled(true);
        sectionGuiElement.findElementById("disconnectButton", ButtonGuiElement.class).setDisabled(false);
        sectionGuiElement.findElementById("updateTimeButton", ButtonGuiElement.class).setDisabled(false);
      }, false),
      buildButton("updateTimeButton", "Update Time via Socket", () -> {
        platform().getSocketClient().sendMessage(new RequestTimeMessage());
      }, true),
      buildButton("updateTimeButtonRest", "Update Time via Rest", () -> {
        platform().getRestClient().get("http://localhost:1381/time", response -> {
          solaGraphics.guiDocument().findElementById("updateTimeButtonRest", ButtonGuiElement.class)
            .findElementsByType(TextGuiElement.class)
            .get(0)
            .setText(new Date(response.body().asObject().getLong("time")).toString());
        });
      }, false),
      buildButton("disconnectButton", "Disconnect", () -> {
        platform().getSocketClient().disconnect();

        solaEcs.setWorld(LevelBuilder.createWorld(MAX_PLAYERS));

        sectionGuiElement.findElementById("connectButton", ButtonGuiElement.class).setDisabled(false);
        sectionGuiElement.findElementById("disconnectButton", ButtonGuiElement.class).setDisabled(true);
        sectionGuiElement.findElementById("updateTimeButton", ButtonGuiElement.class).setDisabled(true);
      }, true)
    );

    return sectionGuiElement;
  }

  private GuiElement<?, ?> buildButton(String id, String text, Runnable onAction, boolean isDisabled) {
    return new ButtonGuiElement()
      .setId(id)
      .setDisabled(isDisabled)
      .setOnAction(onAction)
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setWidth(200)
          .setPadding(15)
          .build()
      ))
      .appendChildren(
        new TextGuiElement().setText(text)
      );
  }
}
