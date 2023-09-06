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
      TextStyles.create().setBackgroundColor(Color.RED).build(),

      new TextStyles.Builder<>().setBackgroundColor(Color.WHITE).build(),
      new TextStyles.Builder<>().setTextColor(Color.RED).build(),
      new TextStyles.Builder<>().setTextColor(Color.BLUE).setBackgroundColor(Color.BLACK).build()
    );

    assertEquals(Color.BLACK, styleContainer.getPropertyValue(TextStyles::getBackgroundColor));
    assertEquals(Color.BLUE, styleContainer.getPropertyValue(TextStyles::getTextColor));

    styleContainer.setStyles(
      new TextStyles.Builder<>().setBackgroundColor(Color.WHITE).build(),
      new TextStyles.Builder<>().setTextColor(Color.RED).build()
    );

    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::getBackgroundColor));
    assertEquals(Color.RED, styleContainer.getPropertyValue(TextStyles::getTextColor));
  }
}
