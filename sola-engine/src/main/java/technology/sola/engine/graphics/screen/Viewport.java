package technology.sola.engine.graphics.screen;

import org.jspecify.annotations.NullMarked;

/**
 * Viewport contains various information about the current viewport used. This is primarily used to keep track of the
 * correct {@link AspectRatioSizing} based on the current {@link AspectMode} and screen width and height.
 */
@NullMarked
public class Viewport {
  private final int canvasWidth;
  private final int canvasHeight;
  private AspectMode aspectMode = AspectMode.IGNORE_RESIZING;
  private int previousScreenWidth;
  private int previousScreenHeight;
  private AspectRatioSizing aspectRatioSizing;
  private float rendererToAspectRatioX = 1;
  private float rendererToAspectRatioY = 1;

  /**
   * Creates an instance of the viewport given the canvas's width and height.
   *
   * @param canvasWidth  the width of the canvas (renderer)
   * @param canvasHeight the height of the canvas (renderer)
   */
  public Viewport(int canvasWidth, int canvasHeight) {
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;

    aspectRatioSizing = new AspectRatioSizing(0, 0, canvasWidth, canvasHeight);
    resize(canvasWidth, canvasHeight);
  }

  /**
   * @return the current {@link AspectMode}
   */
  public AspectMode getAspectMode() {
    return aspectMode;
  }

  /**
   * Updates the {@link AspectMode} used and recalculates {@link AspectRatioSizing}.
   *
   * @param aspectMode the new {@code AspectMode}
   */
  public void setAspectMode(AspectMode aspectMode) {
    this.aspectMode = aspectMode;
    recalculateAspectRatioSizing(previousScreenWidth, previousScreenHeight);
  }

  /**
   * @return the current {@link AspectRatioSizing}
   */
  public AspectRatioSizing getAspectRatioSizing() {
    return aspectRatioSizing;
  }

  /**
   * @return the ratio of {@link Viewport#canvasWidth} to {@link AspectRatioSizing#width()}
   */
  public float getRendererToAspectRatioX() {
    return rendererToAspectRatioX;
  }

  /**
   * @return the ratio of {@link Viewport#canvasHeight} to {@link AspectRatioSizing#height()}
   */
  public float getRendererToAspectRatioY() {
    return rendererToAspectRatioY;
  }

  /**
   * Recalculates the {@link AspectRatioSizing} using the current {@link AspectMode} and the new screen width and height.
   *
   * @param screenWidth  the width of the screen
   * @param screenHeight the height of the screen
   */
  public void resize(int screenWidth, int screenHeight) {
    if (screenWidth == previousScreenWidth && screenHeight == previousScreenHeight) {
      return;
    }

    recalculateAspectRatioSizing(screenWidth, screenHeight);

    previousScreenWidth = screenWidth;
    previousScreenHeight = screenHeight;
  }

  private void recalculateAspectRatioSizing(int screenWidth, int screenHeight) {
    switch (aspectMode) {
      case IGNORE_RESIZING -> aspectRatioSizing = new AspectRatioSizing(0, 0, canvasWidth, canvasHeight);
      case STRETCH -> aspectRatioSizing = new AspectRatioSizing(0, 0, screenWidth, screenHeight);
      case MAINTAIN -> {
        float rendererAspectRatio = canvasWidth / (float) canvasHeight;
        float canvasAspectRatio = screenWidth / (float) screenHeight;
        float scaleFactor = canvasAspectRatio > rendererAspectRatio
          ? screenHeight / (float) canvasHeight
          : screenWidth / (float) canvasWidth;
        int adjustedWidth = (int) (canvasWidth * scaleFactor);
        int adjustedHeight = (int) (canvasHeight * scaleFactor);
        aspectRatioSizing = new AspectRatioSizing(
          (screenWidth - adjustedWidth) / 2, (screenHeight - adjustedHeight) / 2,
          adjustedWidth, adjustedHeight
        );
      }
      default -> {
      }
    }

    rendererToAspectRatioX = canvasWidth / (float) aspectRatioSizing.width();
    rendererToAspectRatioY = canvasHeight / (float) aspectRatioSizing.height();
  }
}
