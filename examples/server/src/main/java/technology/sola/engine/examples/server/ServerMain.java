package technology.sola.engine.examples.server;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaPhysics;
import technology.sola.engine.event.Event;
import technology.sola.engine.examples.common.networking.LevelBuilder;
import technology.sola.engine.examples.common.networking.NetworkingExample;
import technology.sola.engine.examples.common.networking.PlayerComponent;
import technology.sola.engine.examples.common.networking.messages.*;
import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.server.ClientConnection;
import technology.sola.engine.server.SolaServer;
import technology.sola.engine.server.rest.SolaResponse;
import technology.sola.json.JsonObject;
import technology.sola.math.linear.Vector2D;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Runs the example server listening on port 60000.
 */
public class ServerMain {
  private static final int MAX_PLAYERS = 10;

  /**
   * Entry point for Server example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaServer solaServer = new ExampleSolaServer();

    solaServer.start();
  }

  private static class ExampleSolaServer extends SolaServer {
    protected ExampleSolaServer() {
      super(30);
    }

    @Override
    public void initialize() {
      registerRestRoutes();

      SolaPhysics solaPhysics = new SolaPhysics(eventHub);

      solaEcs.addSystems(solaPhysics.getSystems());
      solaEcs.addSystem(new ClientUpdateSystem());
      solaEcs.addSystem(new PlayerMovementSystem());
      solaEcs.setWorld(LevelBuilder.createWorld(NetworkingExample.MAX_PLAYERS));
    }

    @Override
    public int getRestPort() {
      return 1381;
    }

    @Override
    public int getSocketPort() {
      return 1380;
    }

    @Override
    public boolean isAllowedConnection(ClientConnection clientConnection) {
      return getClientConnectionMap().size() < MAX_PLAYERS;
    }

    @Override
    public void onConnectionEstablished(ClientConnection clientConnection) {
      message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));

      solaEcs.getWorld().createEntity(
        String.valueOf(clientConnection.getClientId()),
        "player-" + clientConnection.getClientId(),
        new TransformComponent(400, 400, 25),
        new DynamicBodyComponent(),
        ColliderComponent.circle(),
        new PlayerComponent()
      );
    }

    @Override
    public void onDisconnect(ClientConnection clientConnection) {
      System.out.println("Disconnected - " + clientConnection.getClientId());

      solaEcs.getWorld()
        .findEntityByUniqueId(String.valueOf(clientConnection.getClientId()))
        .setDisabled(true)
        .destroy();

      broadcast(new PlayerRemovedMessage(clientConnection.getClientId()), clientConnection.getClientId());
    }

    @Override
    public boolean onMessage(ClientConnection clientConnection, SocketMessage socketMessage) {
      MessageType messageType = MessageType.values()[socketMessage.getType()];

      switch (messageType) {
        case REQUEST_TIME -> message(clientConnection.getClientId(), new UpdateTimeMessage(System.currentTimeMillis()));
        case PLAYER_MOVE -> {
          PlayerMoveMessage playerMoveMessage = PlayerMoveMessage.parse(socketMessage);

          eventHub.emit(new PlayerMoveEvent(String.valueOf(clientConnection.getClientId()), playerMoveMessage.getDirection()));
        }
      }

      return true;
    }

    private record PlayerMoveEvent(String playerId, int direction) implements Event {
    }

    private class PlayerMovementSystem extends EcsSystem {
      private final Map<String, Integer> directionMap = new HashMap<>();

      private PlayerMovementSystem() {
        eventHub.add(PlayerMoveEvent.class, event -> {
          directionMap.put(event.playerId, event.direction);
        });
      }

      @Override
      public void update(World world, float v) {
        var iter = directionMap.entrySet().iterator();

        while (iter.hasNext()) {
          var entry = iter.next();
          Entity entity = world.findEntityByUniqueId(entry.getKey());

          if (entity != null) {
            DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);
            Vector2D velocity = dynamicBodyComponent.getVelocity();

            if (entry.getValue() == 0) {
              dynamicBodyComponent.setVelocity(new Vector2D(0, velocity.y()));
            } else if (entry.getValue() == 1) {
              dynamicBodyComponent.setVelocity(new Vector2D(-25, velocity.y()));
            } else if (entry.getValue() == 2) {
              dynamicBodyComponent.setVelocity(new Vector2D(25, velocity.y()));
            }
          }

          iter.remove();
        }
      }

      @Override
      public int getOrder() {
        return -999;
      }
    }

    private class ClientUpdateSystem extends EcsSystem {
      @Override
      public void update(World world, float v) {
        broadcast(new PlayerPositionUpdatesMessage(
          world.createView().of(TransformComponent.class, PlayerComponent.class)
            .getEntries()
            .stream()
            .map(entry -> new PlayerPositionUpdatesMessage.PlayerPosition(entry.entity().getUniqueId(), entry.c1().getTranslate()))
            .collect(Collectors.toList())
        ));
      }

      @Override
      public int getOrder() {
        return 999;
      }
    }

    private void registerRestRoutes() {
      solaRouter.get( "/", solaRequest -> new SolaResponse(200, new JsonObject()));

      solaRouter.get("/test", solaRequest -> {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("test", solaRequest.queryParameters().getOrDefault("test", "missing"));

        return new SolaResponse(200, jsonObject);
      });

      solaRouter.get("/time", solaRequest -> {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("time", System.currentTimeMillis());

        return new SolaResponse(200, jsonObject);
      });

      solaRouter.get("/test/{myTestParam}", solaRequest -> {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("myTestParam", solaRequest.pathParameters().get("myTestParam"));

        return new SolaResponse(200, jsonObject);
      });

      solaRouter.post("/test", solaRequest -> new SolaResponse(200, solaRequest.body()));
    }
  }
}
