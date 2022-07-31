package technology.sola.engine.platform.swing.assets.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class AudioClipException extends RuntimeException {
  private static final Logger LOGGER = LoggerFactory.getLogger(AudioClipException.class);
  @Serial
  private static final long serialVersionUID = 4335657918705890925L;

  public AudioClipException(String message) {
    super(message);
    LOGGER.error(message);
  }
}
