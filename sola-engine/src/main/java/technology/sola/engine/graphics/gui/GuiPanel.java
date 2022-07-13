package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;

import java.util.List;

// TODO consider ability to add a sub GuiPanel?

public class GuiPanel {
  private int x;
  private int y;
  private int width;
  private int height;

  private Direction direction = Direction.HORIZONTAL;
  private Anchor anchor = Anchor.TOP_LEFT;

  private List<GuiElement> guiElementList;

  public GuiPanel(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void render(Renderer renderer) {

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
