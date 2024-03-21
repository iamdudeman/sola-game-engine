package technology.sola.engine.examples.common.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPositionUpdatesMessage extends SocketMessage {
  private final List<PlayerPosition> playerPositions;

  public PlayerPositionUpdatesMessage(List<PlayerPosition> playerPositions) {
    super(
      MessageType.PLAYER_POSITION_UPDATES.ordinal(),
      playerPositions.stream()
        .map(playerPosition -> playerPosition.id + "_" + playerPosition.translate.x() + "_" + playerPosition.translate.y())
        .collect(Collectors.joining("!"))
    );
    this.playerPositions = playerPositions;
  }

  public static PlayerPositionUpdatesMessage parse(SocketMessage socketMessage) {
    String[] playerDetails = socketMessage.getBody().split("!");
    List<PlayerPosition> playerPositions = new ArrayList<>(playerDetails.length);

    for (String playerDetail : playerDetails) {
      String[] detailParts = playerDetail.split("_");

      playerPositions.add(new PlayerPosition(detailParts[0], new Vector2D(
        Float.parseFloat(detailParts[1]), Float.parseFloat(detailParts[2])
      )));
    }

    return new PlayerPositionUpdatesMessage(playerPositions);
  }

  public List<PlayerPosition> getPlayerPositions() {
    return playerPositions;
  }

  public record PlayerPosition(String id, Vector2D translate) {
  }
}
