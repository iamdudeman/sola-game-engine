package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.properties.GuiElementBounds;

import java.util.List;

/**
 * The node of the Gui Tree
 */
public class GuiContainer extends GuiElement {
  private List<GuiElement> children;
  private int width;
  private int height;
  private Direction direction = Direction.HORIZONTAL;
  private Anchor anchor = Anchor.TOP_LEFT;

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void render(Renderer renderer, int x, int y) {

  }

  public enum Direction {
    HORIZONTAL,
    VERTICAL,
  }

  public enum Anchor {
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    CENTER,
    TOP_LEFT,
    TOP_RIGHT
  }
}
