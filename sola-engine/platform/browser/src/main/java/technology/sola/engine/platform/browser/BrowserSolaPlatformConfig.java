package technology.sola.engine.platform.browser;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatformCommonConfig;
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
  ) implements SolaPlatformCommonConfig {
  /**
   * Default configuration that has useSoftwareRendering set to true.
   */
  public BrowserSolaPlatformConfig() {
    this(true, Color.BLACK, true);
  }
}
