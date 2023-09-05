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
      new TextStyles().setBackgroundColor(Color.WHITE),
      new TextStyles().setTextColor(Color.RED),
      new TextStyles().setTextColor(Color.BLUE).setBackgroundColor(Color.BLACK)
    );

    assertEquals(Color.BLACK, styleContainer.getPropertyValue(TextStyles::getBackgroundColor));
    assertEquals(Color.BLUE, styleContainer.getPropertyValue(TextStyles::getTextColor));

    styleContainer.setStyles(
      new TextStyles().setBackgroundColor(Color.WHITE),
      new TextStyles().setTextColor(Color.RED)
    );

    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::getBackgroundColor));
    assertEquals(Color.RED, styleContainer.getPropertyValue(TextStyles::getTextColor));
  }
}
