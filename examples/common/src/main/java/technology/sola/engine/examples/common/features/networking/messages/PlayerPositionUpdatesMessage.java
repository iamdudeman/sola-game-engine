package technology.sola.engine.examples.common.features.networking.messages;

import technology.sola.engine.networking.socket.SocketMessage;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link SocketMessage} to let clients know the positions of all connected players.
 */
public class PlayerPositionUpdatesMessage extends SocketMessage {
  private final List<PlayerPosition> playerPositions;

  /**
   * Creates a message with the player positions.
   *
   * @param playerPositions the player positions
   */
  public PlayerPositionUpdatesMessage(List<PlayerPosition> playerPositions) {
    super(
      MessageType.PLAYER_POSITION_UPDATES.ordinal(),
      playerPositions.stream()
        .map(playerPosition -> playerPosition.id + "_" + playerPosition.translate.x() + "_" + playerPosition.translate.y())
        .collect(Collectors.joining("!"))
    );
    this.playerPositions = playerPositions;
  }

  /**
   * Parses a PlayerPositionUpdatesMessage from a generic SocketMessage.
   *
   * @param socketMessage the socket message to parse
   * @return the PlayerPositionUpdatesMessage
   */
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

  /**
   * @return the list of player positions
   */
  public List<PlayerPosition> getPlayerPositions() {
    return playerPositions;
  }

  /**
   * Container for a player's id and position.
   *
   * @param id        the player id
   * @param translate the player position
   */
  public record PlayerPosition(String id, Vector2D translate) {
  }
}
