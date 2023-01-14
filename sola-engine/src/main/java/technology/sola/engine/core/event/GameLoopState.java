package technology.sola.engine.core.event;

/**
 * The current state of the {@link technology.sola.engine.core.GameLoop}.
 */
public enum GameLoopState {
  /**
   * The game loop has begun the process to stop.
   */
  STOP,
  /**
   * The game loop has finished stopping.
   */
  STOPPED,
  /**
   * The game loop is not currently running but can be resumed.
   */
  PAUSE,
  /**
   * The game loop has begun resuming.
   */
  RESUME,
}
