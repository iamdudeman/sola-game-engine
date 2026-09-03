package technology.sola.engine.platform.browser;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;

/**
 * Configuration for the {@link BrowserSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.browser.core.BrowserCanvasRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param useImageSmoothing    whether image smoothing that uses higher quality filtering when scaling images rendered on the internal {Canvas}
 */
@NullMarked
public record BrowserSolaPlatformConfig(
  boolean useSoftwareRendering,
  Color backgroundColor,
  boolean useImageSmoothing
) implements SolaPlatformConfig {
  /**
   * Default configuration that has useSoftwareRendering set to true.
   */
  public BrowserSolaPlatformConfig() {
    this(true, Color.BLACK, true);
  }

  /**
   * Sets whether software rendering should be used.
   *
   * @param useSoftwareRendering whether software rendering should be used
   * @return a new instance with the software rendering setting updated
   */
  public BrowserSolaPlatformConfig setSoftwareRendering(boolean useSoftwareRendering) {
    return new BrowserSolaPlatformConfig(useSoftwareRendering, backgroundColor, useImageSmoothing);
  }

  /**
   * Sets the background {@link Color}.
   *
   * @param backgroundColor the background color
   * @return a new instance with the background color updated
   */
  public BrowserSolaPlatformConfig setBackgroundColor(Color backgroundColor) {
    return new BrowserSolaPlatformConfig(useSoftwareRendering, backgroundColor, useImageSmoothing);
  }

  /**
   * Sets whether image smoothing should be used.
   *
   * @param useImageSmoothing whether image smoothing should be used
   * @return a new instance with the image smoothing setting updated
   */
  public BrowserSolaPlatformConfig setImageSmoothing(boolean useImageSmoothing) {
    return new BrowserSolaPlatformConfig(useSoftwareRendering, backgroundColor, useImageSmoothing);
  }
}
