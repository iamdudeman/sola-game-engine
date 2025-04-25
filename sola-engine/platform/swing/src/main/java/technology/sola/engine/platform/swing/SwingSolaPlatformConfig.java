package technology.sola.engine.platform.swing;

import java.awt.*;

/**
 * Configuration for the {@link SwingSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.swing.core.Graphics2dRenderer} rendering
 * @param initialWindowSize    the initial window size when the {@link technology.sola.engine.core.Sola} starts
 */
public record SwingSolaPlatformConfig(
  boolean useSoftwareRendering,
  Dimension initialWindowSize
) {
  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set.
   */
  public SwingSolaPlatformConfig() {
    this(true, null);
  }
}
