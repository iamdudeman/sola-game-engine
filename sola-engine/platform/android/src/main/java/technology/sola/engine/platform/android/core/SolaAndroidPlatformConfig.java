package technology.sola.engine.platform.android.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.platform.android.config.Orientation;

@NullMarked
public record SolaAndroidPlatformConfig(
  Orientation orientation
) {
  public SolaAndroidPlatformConfig() {
    this(Orientation.PORTRAIT);
  }
}
