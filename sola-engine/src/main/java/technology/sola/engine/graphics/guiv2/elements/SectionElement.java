package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

public class SectionElement extends GuiElement<BaseStyles> {
  public SectionElement(BaseStyles... styles) {
    super(styles);
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public void onRecalculateLayout() {

  }
}
