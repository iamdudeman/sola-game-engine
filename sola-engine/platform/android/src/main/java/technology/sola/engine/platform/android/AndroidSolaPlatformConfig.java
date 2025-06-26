package technology.sola.engine.platform.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.platform.android.config.Orientation;

/**
 * Configuration for the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 *
 * @param orientation the {@link Orientation} of the app
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.android.core.AndroidRenderer} rendering
 */
@NullMarked
public record AndroidSolaPlatformConfig(
  Orientation orientation,
  boolean useSoftwareRendering
) {
  /**
   * Creates an instance with default options. Orientation is set to {@link Orientation#PORTRAIT}.
   */
  public AndroidSolaPlatformConfig() {
    this(Orientation.PORTRAIT, true);
  }
}
