package technology.sola.engine.platform.javafx;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.core.SolaPlatformCommonConfig;
import technology.sola.engine.graphics.Color;

/**
 * Configuration for the {@link JavaFxSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.javafx.core.JavaFxRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param useImageSmoothing    whether image smoothing which uses higher quality filtering when scaling images rendered on the internal {@link javafx.scene.canvas.Canvas}
 * @param initialWindowWidth   the initial window width when the {@link technology.sola.engine.core.Sola} starts
 * @param initialWindowHeight  the initial window height when the {@link technology.sola.engine.core.Sola} starts
 */
@NullMarked
public record JavaFxSolaPlatformConfig(
  boolean useSoftwareRendering,
  Color backgroundColor,
  boolean useImageSmoothing,
  @Nullable Double initialWindowWidth,
  @Nullable Double initialWindowHeight
) implements SolaPlatformCommonConfig {
  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set. Image smoothing is set
   * to smooth.
   */
  public JavaFxSolaPlatformConfig() {
    this(true, Color.BLACK, true, null, null);
  }
}
