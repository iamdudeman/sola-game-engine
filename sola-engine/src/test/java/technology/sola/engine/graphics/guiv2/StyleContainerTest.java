package technology.sola.engine.graphics.guiv2;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleContainerTest {
  @Test
  void test() {
    var guiElement = Mockito.mock(TextGuiElement.class);
    var styleContainer = new StyleContainer<>(guiElement);

    styleContainer.setStyles(
      List.of(
        ConditionalStyle.always(TextStyles.create().setBackgroundColor(Color.WHITE).build()),
        ConditionalStyle.always(TextStyles.create().setTextColor(Color.RED).build()),
        ConditionalStyle.always(TextStyles.create().setTextColor(Color.BLUE).setBackgroundColor(Color.BLACK).build())
      )
    );

    assertEquals(Color.BLACK, styleContainer.getPropertyValue(TextStyles::background).color());
    assertEquals(Color.BLUE, styleContainer.getPropertyValue(TextStyles::textColor));

    styleContainer.setStyles(
      List.of(
        ConditionalStyle.always(TextStyles.create().setBackgroundColor(Color.WHITE).build()),
        ConditionalStyle.always(TextStyles.create().setTextColor(Color.RED).build()),
        ConditionalStyle.hover(TextStyles.create().setTextColor(Color.WHITE).build())
      )
    );

    Mockito.when(guiElement.isHovered()).thenReturn(false);
    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::background).color());
    assertEquals(Color.RED, styleContainer.getPropertyValue(TextStyles::textColor));

    Mockito.when(guiElement.isHovered()).thenReturn(true);
    styleContainer.invalidate();
    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::background).color());
    assertEquals(Color.WHITE, styleContainer.getPropertyValue(TextStyles::textColor));
  }
}
