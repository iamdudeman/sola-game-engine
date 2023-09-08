package technology.sola.engine.graphics.guiv2;

import org.junit.jupiter.api.Test;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleContainerTest {
  @Test
  void test() {
    var styleContainer = new StyleContainer<>(
      TextStyles.create().setBackgroundColor(Color.WHITE).build(),
      TextStyles.create().setTextColor(Color.RED).build(),
      TextStyles.create().setTextColor(Color.BLUE).setBackgroundColor(Color.BLACK).build()
    );

    assertEquals(Color.BLACK, styleContainer.getPropertyValue(TextStyles::background).color());
    assertEquals(Color.BLUE, styleContainer.getPropertyValue(TextStyles::getTextColor));

    styleContainer.setStyles(
      TextStyles.create().setBackgroundColor(Color.WHITE),
      TextStyles.create().setTextColor(Color.RED)
    );

    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::background).color());
    assertEquals(Color.RED, styleContainer.getPropertyValue(TextStyles::getTextColor));
  }
}
