package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * TouchPhase enum contains information about the phase of a touch gesture.
 */
@NullMarked
public enum TouchPhase {
  /**
   * Touch gesture has started by pressing a finger on the screen.
   */
  BEGAN,
  /**
   * Touch gesture continues for a finger already on the screen.
   */
  MOVED,
  /**
   * Touch gesture has ended by removing a finger from the screen.
   */
  ENDED,
  /**
   * Touch gesture that had {@link TouchPhase#BEGAN} was not completed.
   */
  CANCELLED,
  /**
   * Touch gesture that had {@link TouchPhase#BEGAN} and is not moving. This may not be used by
   * all {@link technology.sola.engine.core.SolaPlatform}s.
   */
  STATIONARY
}
