package technology.sola.engine.platform.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatformCommonConfig;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.platform.android.config.Orientation;

/**
 * Configuration for the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.android.core.AndroidRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param orientation the {@link Orientation} of the app
 */
@NullMarked
public record AndroidSolaPlatformConfig(
  boolean useSoftwareRendering,
  Color backgroundColor,
  Orientation orientation
) implements SolaPlatformCommonConfig {
  /**
   * Creates an instance with default options. Orientation is set to {@link Orientation#PORTRAIT}.
   */
  public AndroidSolaPlatformConfig() {
    this(true, Color.BLACK, Orientation.PORTRAIT);
  }
}
