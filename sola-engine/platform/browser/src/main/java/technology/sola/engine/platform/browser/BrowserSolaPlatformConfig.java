package technology.sola.engine.platform.browser;

import org.jspecify.annotations.NullMarked;

/**
 * Configuration for the {@link BrowserSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.browser.core.BrowserCanvasRenderer} rendering
 */
@NullMarked
public record BrowserSolaPlatformConfig(
  boolean useSoftwareRendering
) {
  /**
   * Default configuration which has useSoftwareRendering set to true.
   */
  public BrowserSolaPlatformConfig() {
    this(true);
  }
}
