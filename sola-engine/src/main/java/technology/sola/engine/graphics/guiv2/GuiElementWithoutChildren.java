package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

public abstract class GuiElementWithoutChildren<Style extends BaseStyles> extends GuiElement<Style> {
  @SafeVarargs
  public GuiElementWithoutChildren(Style... styles) {
    super(styles);
  }

  @Override
  public GuiElement<Style> appendChildren(GuiElement<?>... children) {
    return this;
  }

  @Override
  protected void renderChildren(Renderer renderer) {
  }
}
