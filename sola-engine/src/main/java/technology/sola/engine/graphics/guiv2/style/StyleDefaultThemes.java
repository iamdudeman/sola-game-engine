package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;

// todo finish implementing this concept

public class StyleDefaultThemes {
  public static class Light {
    public static BaseStyles.Builder<?> createBaseStyles() {
      return BaseStyles.create()
        .setBackgroundColor(Color.WHITE);
    }

    public static BaseStyles.Builder<?> createTextStyles() {
      return TextStyles.create()
        .setTextColor(Color.BLACK);
    }

    // todo default input styles (placeholder - colorText.tint(0.5f))
  }

  public static class Dark {
    public static BaseStyles.Builder<?> createBaseStyles() {
      return BaseStyles.create()
        .setBackgroundColor(Color.WHITE);
    }

    public static BaseStyles.Builder<?> createTextStyles() {
      return TextStyles.create()
        .setTextColor(Color.WHITE);
    }

    // todo default input styles (placeholder - colorText.shade(0.2f))

  }
}
