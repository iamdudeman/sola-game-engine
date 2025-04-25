package technology.sola.engine.platform.browser;

/**
 * Configuration for the {@link BrowserSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native canvas rendering
 */
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
