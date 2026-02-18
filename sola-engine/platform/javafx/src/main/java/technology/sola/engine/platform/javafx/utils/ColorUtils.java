package technology.sola.engine.platform.javafx.utils;

import technology.sola.engine.graphics.Color;

/**
 * ColorUtil contains utility methods for dealing with Sola and JavaFX colors.
 */
public class ColorUtils {
  /**
   * Converts a JavaFX color to a Sola color.
   *
   * @param javaFxColor the JavaFX color to convert
   * @return the Sola color
   */
  public static Color toSolaColor(javafx.scene.paint.Color javaFxColor) {
    return new Color(
      (int) (javaFxColor.getOpacity() * 255f),
      (int) (javaFxColor.getRed() * 255),
      (int) (javaFxColor.getGreen() * 255),
      (int) (javaFxColor.getBlue() * 255)
    );
  }

  /**
   * Converts a Sola color to a JavaFX color.
   *
   * @param solaColor the Sola color to convert
   * @return the JavaFX color
   */
  public static javafx.scene.paint.Color toJavaFxColor(Color solaColor) {
    return javafx.scene.paint.Color.rgb(
      solaColor.getRed(),
      solaColor.getGreen(),
      solaColor.getBlue(),
      solaColor.getAlpha() / 255f
    );
  }

  private ColorUtils() {
  }
}
