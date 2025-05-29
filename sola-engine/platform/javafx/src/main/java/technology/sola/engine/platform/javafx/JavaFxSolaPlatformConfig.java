package technology.sola.engine.platform.javafx;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Configuration for the {@link JavaFxSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.javafx.core.JavaFxRenderer} rendering
 * @param useImageSmoothing    whether image smoothing which uses higher quality filtering when scaling images rendered on the internal {@link javafx.scene.canvas.Canvas}
 * @param initialWindowWidth   the initial window width when the {@link technology.sola.engine.core.Sola} starts
 * @param initialWindowHeight  the initial window height when the {@link technology.sola.engine.core.Sola} starts
 */
@NullMarked
public record JavaFxSolaPlatformConfig(
  boolean useSoftwareRendering,
  boolean useImageSmoothing,
  @Nullable Double initialWindowWidth,
  @Nullable Double initialWindowHeight
) {
  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set. Image smoothing is set
   * to smooth.
   */
  public JavaFxSolaPlatformConfig() {
    this(true, true, null, null);
  }
}
