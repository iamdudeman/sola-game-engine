package technology.sola.engine.platform.android.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.platform.android.config.Orientation;

/**
 * Configuration for the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 *
 * @param orientation the {@link Orientation} of the app
 */
@NullMarked
public record SolaAndroidPlatformConfig(
  Orientation orientation
) {
  /**
   * Creates an instance with default options. Orientation is set to {@link Orientation#PORTRAIT}.
   */
  public SolaAndroidPlatformConfig() {
    this(Orientation.PORTRAIT);
  }
}
