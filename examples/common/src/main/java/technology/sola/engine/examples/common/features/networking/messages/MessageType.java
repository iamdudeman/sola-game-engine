package technology.sola.engine.examples.common.features.networking.messages;

import technology.sola.engine.examples.common.features.networking.NetworkingExample;

/**
 * MessageType contains the ids of valid network message types for
 * {@link NetworkingExample}.
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
  /**
   * Message to notify clients of all the positions of players.
   */
  PLAYER_POSITION_UPDATES,
  /**
   * Message to notify a player wants to move.
   */
  PLAYER_MOVE,
  /**
   * Message to notify a player's position changed.
   */
  PLAYER_UPDATE,
}
