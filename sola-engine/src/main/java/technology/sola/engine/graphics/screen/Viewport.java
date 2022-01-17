package technology.sola.engine.graphics.screen;

public class Viewport {
  private final int canvasWidth;
  private final int canvasHeight;
  private AspectMode aspectMode = AspectMode.IGNORE_RESIZING;
  private int previousScreenWidth;
  private int previousScreenHeight;
  private AspectRatioSizing aspectRatioSizing;

  public Viewport(int canvasWidth, int canvasHeight) {
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;
    resize(canvasWidth, canvasHeight);
  }

  public void setAspectMode(AspectMode aspectMode) {
    this.aspectMode = aspectMode;
    recalculateAspectRatioSizing(previousScreenWidth, previousScreenHeight);
  }

  public AspectRatioSizing getAspectRatioSizing() {
    return aspectRatioSizing;
  }

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
  }
}
