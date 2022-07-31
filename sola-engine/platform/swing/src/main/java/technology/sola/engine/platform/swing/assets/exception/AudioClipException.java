package technology.sola.engine.platform.swing.assets.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioClipException extends RuntimeException {
  private static final Logger LOGGER = LoggerFactory.getLogger(AudioClipException.class);

  public AudioClipException(String message) {
    super(message);
    LOGGER.error(message);
  }
}
