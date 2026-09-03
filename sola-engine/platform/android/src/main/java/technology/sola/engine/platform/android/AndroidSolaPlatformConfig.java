package technology.sola.engine.platform.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.platform.android.config.Orientation;

/**
 * Configuration for the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 *
 * @param useSoftwareRendering whether software rendering should be used instead of native {@link technology.sola.engine.platform.android.core.AndroidRenderer} rendering
 * @param backgroundColor      the background color that will be cleared to every frame
 * @param orientation          the {@link Orientation} of the app
 * @param useImageSmoothing    whether image smoothing that uses higher quality filtering when scaling images rendered on the internal {@link android.graphics.Canvas}
 */
@NullMarked
public record AndroidSolaPlatformConfig(
  boolean useSoftwareRendering,
  Color backgroundColor,
  Orientation orientation,
  boolean useImageSmoothing
) implements SolaPlatformConfig {
  /**
   * Creates an instance with default options. Orientation is set to {@link Orientation#PORTRAIT}.
   */
  public AndroidSolaPlatformConfig() {
    this(true, Color.BLACK, Orientation.PORTRAIT, true);
  }

  /**
   * Sets whether software rendering should be used.
   *
   * @param useSoftwareRendering whether software rendering should be used
   * @return a new instance with the software rendering setting updated
   */
  public AndroidSolaPlatformConfig setSoftwareRendering(boolean useSoftwareRendering) {
    return new AndroidSolaPlatformConfig(useSoftwareRendering, backgroundColor, orientation, useImageSmoothing);
  }

  /**
   * Sets the background {@link Color}.
   *
   * @param backgroundColor the background color
   * @return a new instance with the background color updated
   */
  public AndroidSolaPlatformConfig setBackgroundColor(Color backgroundColor) {
    return new AndroidSolaPlatformConfig(useSoftwareRendering, backgroundColor, orientation, useImageSmoothing);
  }

  /**
   * Sets the  {@link Orientation}.
   *
   * @param orientation the orientation
   * @return a new instance with the orientation updated
   */
  public AndroidSolaPlatformConfig setOrientation(Orientation orientation) {
    return new AndroidSolaPlatformConfig(useSoftwareRendering, backgroundColor, orientation, useImageSmoothing);
  }

  /**
   * Sets whether image smoothing should be used.
   *
   * @param useImageSmoothing whether image smoothing should be used
   * @return a new instance with the image smoothing setting updated
   */
  public AndroidSolaPlatformConfig setImageSmoothing(boolean useImageSmoothing) {
    return new AndroidSolaPlatformConfig(useSoftwareRendering, backgroundColor, orientation, useImageSmoothing);
  }
}
