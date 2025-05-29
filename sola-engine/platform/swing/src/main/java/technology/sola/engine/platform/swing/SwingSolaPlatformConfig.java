package technology.sola.engine.platform.swing;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.awt.*;

/**
 * Configuration for the {@link SwingSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.swing.core.Graphics2dRenderer} rendering
 * @param initialWindowSize    the initial window size when the {@link technology.sola.engine.core.Sola} starts
 */
@NullMarked
public record SwingSolaPlatformConfig(
  boolean useSoftwareRendering,
  @Nullable Dimension initialWindowSize
) {
  /**
   * Configuration for the {@link SwingSolaPlatform}.
   *
   * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.swing.core.Graphics2dRenderer} rendering
   * @param initialWindowWidth   the initial window width when the {@link technology.sola.engine.core.Sola} starts
   * @param initialWindowHeight  the initial window width when the {@link technology.sola.engine.core.Sola} starts
   */
  public SwingSolaPlatformConfig(boolean useSoftwareRendering, int initialWindowWidth, int initialWindowHeight) {
    this(useSoftwareRendering, new Dimension(initialWindowWidth, initialWindowHeight));
  }

  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set.
   */
  public SwingSolaPlatformConfig() {
    this(true, null);
  }
}
