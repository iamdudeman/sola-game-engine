package technology.sola.engine.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * SolaPlatformCommonConfig defines common configuration options for all {@link SolaPlatform}s.
 */
@NullMarked
public interface SolaPlatformCommonConfig {
  /**
   * Whether software rendering should be used instead of native rendering for the {@link SolaPlatform}.
   *
   * @return true if using software rendering
   */
  boolean useSoftwareRendering();

  /**
   * Sets the color that the SolaPlatform will clear to before rendering.
   *
   * @return the background color
   */
  Color backgroundColor();
}
