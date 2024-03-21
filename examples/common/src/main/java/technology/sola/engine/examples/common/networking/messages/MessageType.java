package technology.sola.engine.examples.common.networking.messages;

/**
 * MessageType contains the ids of valid network message types for
 * {@link technology.sola.engine.examples.common.networking.NetworkingExample}.
 */
public enum MessageType {
  /**
   * Message containing the updated time.
   */
  UPDATE_TIME,
  /**
   * Message to request updated time.
   */
  REQUEST_TIME,
  /**
   * Message to notify a player was removed.
   */
  PLAYER_REMOVED,
  PLAYER_POSITION_UPDATES,
  PLAYER_MOVE,
  /**
   * Message to notify a player's position changed.
   */
  PLAYER_UPDATE,
}
