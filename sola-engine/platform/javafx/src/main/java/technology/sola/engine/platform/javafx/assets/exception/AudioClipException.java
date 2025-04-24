package technology.sola.engine.platform.javafx.assets.exception;

import technology.sola.logging.SolaLogger;

import java.io.Serial;

/**
 * A {@link RuntimeException} that is thrown when an {@link technology.sola.engine.assets.audio.AudioClip} fails
 * to load.
 */
public class AudioClipException extends RuntimeException {
  private static final SolaLogger LOGGER = SolaLogger.of(AudioClipException.class);
  @Serial
  private static final long serialVersionUID = -3588454095968360166L;

  /**
   * Creates an instance of this exception.
   *
   * @param message the error message
   */
  public AudioClipException(String message) {
    super(message);
    LOGGER.error(message);
  }
}
