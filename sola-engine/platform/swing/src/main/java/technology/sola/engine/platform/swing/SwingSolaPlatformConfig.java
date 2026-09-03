package technology.sola.engine.platform.swing;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;

import java.awt.*;

/**
 * Configuration for the {@link SwingSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.swing.core.Graphics2dRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param initialWindowSize    the initial window size when the {@link technology.sola.engine.core.Sola} starts
 */
@NullMarked
public record SwingSolaPlatformConfig(
  boolean useSoftwareRendering,
  Color backgroundColor,
  @Nullable Dimension initialWindowSize
) implements SolaPlatformConfig {
  /**
   * Configuration for the {@link SwingSolaPlatform}.
   *
   * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.swing.core.Graphics2dRenderer} rendering
   * @param initialWindowWidth   the initial window width when the {@link technology.sola.engine.core.Sola} starts
   * @param initialWindowHeight  the initial window width when the {@link technology.sola.engine.core.Sola} starts
   */
  public SwingSolaPlatformConfig(boolean useSoftwareRendering, int initialWindowWidth, int initialWindowHeight) {
    this(useSoftwareRendering, Color.BLACK, new Dimension(initialWindowWidth, initialWindowHeight));
  }

  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set.
   */
  public SwingSolaPlatformConfig() {
    this(true, Color.BLACK, null);
  }

  /**
   * Sets whether software rendering should be used.
   *
   * @param useSoftwareRendering whether software rendering should be used
   * @return a new instance with the software rendering setting updated
   */
  public SwingSolaPlatformConfig setSoftwareRendering(boolean useSoftwareRendering) {
    return new SwingSolaPlatformConfig(useSoftwareRendering, backgroundColor, initialWindowSize);
  }

  /**
   * Sets the background {@link Color}.
   *
   * @param backgroundColor the background color
   * @return a new instance with the background color updated
   */
  public SwingSolaPlatformConfig setBackgroundColor(Color backgroundColor) {
    return new SwingSolaPlatformConfig(useSoftwareRendering, backgroundColor, initialWindowSize);
  }

  /**
   * Sets the initial window size.
   *
   * @param width  the initial window width
   * @param height the initial window height
   * @return a new instance with the initial window size updated
   */
  public SwingSolaPlatformConfig setInitialWindowSize(int width, int height) {
    return new SwingSolaPlatformConfig(useSoftwareRendering, backgroundColor, new Dimension(width, height));
  }
}
