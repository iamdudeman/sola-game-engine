package technology.sola.engine.editor.ui.ecs.rendering;

public class ColorUtils {
  public static javafx.scene.paint.Color toJavaFxColor(technology.sola.engine.graphics.Color color) {
    if (color == null) {
      return null;
    }

    return new javafx.scene.paint.Color(
      color.getRed() / 255f,
      color.getGreen() / 255f,
      color.getBlue() / 255f,
      color.getAlpha() / 255f
    );
  }

  public static technology.sola.engine.graphics.Color toSolaColor(javafx.scene.paint.Color color) {
    if (color == null) {
      return null;
    }

    return new technology.sola.engine.graphics.Color(
      (int)(color.getOpacity() * 255),
      (int)(color.getRed() * 255),
      (int)(color.getGreen() * 255),
      (int)(color.getBlue() * 255)
    );
  }

  private ColorUtils() {
  }
}
