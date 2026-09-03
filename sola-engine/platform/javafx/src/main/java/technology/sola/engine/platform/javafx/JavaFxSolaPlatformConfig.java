package technology.sola.engine.platform.javafx;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;

/**
 * Configuration for the {@link JavaFxSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.javafx.core.JavaFxRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param useImageSmoothing    whether image smoothing that uses higher quality filtering when scaling images rendered on the internal {@link javafx.scene.canvas.Canvas}
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
) implements SolaPlatformConfig {
  /**
   * Default configuration with useSoftwareRendering set to true and no initial window size set. Image smoothing is set
   * to smooth.
   */
  public JavaFxSolaPlatformConfig() {
    this(true, Color.BLACK, true, null, null);
  }

  /**
   * Sets whether software rendering should be used.
   *
   * @param useSoftwareRendering whether software rendering should be used
   * @return a new instance with the software rendering setting updated
   */
  public JavaFxSolaPlatformConfig setSoftwareRendering(boolean useSoftwareRendering) {
    return new JavaFxSolaPlatformConfig(
      useSoftwareRendering, backgroundColor, useImageSmoothing, initialWindowWidth, initialWindowHeight
    );
  }

  /**
   * Sets the background {@link Color}.
   *
   * @param backgroundColor the background color
   * @return a new instance with the background color updated
   */
  public JavaFxSolaPlatformConfig setBackgroundColor(Color backgroundColor) {
    return new JavaFxSolaPlatformConfig(
      useSoftwareRendering, backgroundColor, useImageSmoothing, initialWindowWidth, initialWindowHeight
    );
  }

  /**
   * Sets whether image smoothing should be used.
   *
   * @param useImageSmoothing whether image smoothing should be used
   * @return a new instance with the image smoothing setting updated
   */
  public JavaFxSolaPlatformConfig setImageSmoothing(boolean useImageSmoothing) {
    return new JavaFxSolaPlatformConfig(
      useSoftwareRendering, backgroundColor, useImageSmoothing, initialWindowWidth, initialWindowHeight
    );
  }

  /**
   * Sets the initial window size.
   *
   * @param width  the initial window width
   * @param height the initial window height
   * @return a new instance with the initial window size updated
   */
  public JavaFxSolaPlatformConfig setInitialWindowSize(double width, double height) {
    return new JavaFxSolaPlatformConfig(useSoftwareRendering, backgroundColor, useImageSmoothing, width, height);
  }
}
