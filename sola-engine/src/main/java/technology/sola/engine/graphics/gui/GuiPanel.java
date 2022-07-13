package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO consider ability to add a sub GuiPanel?

public class GuiPanel {
  private int x;
  private int y;
  private int width;
  private int height;

  private Direction direction = Direction.HORIZONTAL;
  private Anchor anchor = Anchor.TOP_LEFT;

  private List<GuiElement> guiElementList = new ArrayList<>();
  private List<GuiElementPosition> guiElementPositions = new ArrayList<>();

  private final Map<GuiElement, Boolean> guiElementMouseOver = new HashMap<>();

  public GuiPanel(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void render(Renderer renderer) {
    guiElementPositions.forEach(guiElementPosition -> {
      if (guiElementPosition.guiElement.properties().isHidden()) {
        return;
      }

      guiElementPosition.guiElement.render(renderer, guiElementPosition.x, guiElementPosition.y);
    });
  }

  public void addGuiElement(GuiElement guiElement) {
    guiElementList.add(guiElement);
    guiElementPositions.add(new GuiElementPosition(guiElement, x + guiElementPositions.size() * guiElement.getWidth(), y));
  }

  public void onMousePressed(MouseEvent event) {
    handleMouseEvent(event, "press");
  }

  public void onMouseReleased(MouseEvent event) {
    handleMouseEvent(event, "release");
  }

  public void onMouseMoved(MouseEvent event) {
    handleMouseEvent(event, "move");
  }

  private void handleMouseEvent(MouseEvent event, String eventType) {
    guiElementPositions.forEach(guiElementPosition -> {
      GuiElement guiElement = guiElementPosition.guiElement;

      if (guiElement.properties().isHidden()) {
        return;
      }

      Integer x = guiElementPosition.x;
      Integer y = guiElementPosition.y;
      Integer width = guiElement.getWidth();
      Integer height = guiElement.getHeight();

      Rectangle guiElementBounds = new Rectangle(new Vector2D(x, y), new Vector2D(x + width, y + height));

      if (guiElementBounds.contains(new Vector2D(event.x(), event.y()))) {
        switch (eventType) {
          case "press" -> guiElement.onMouseDown(event);
          case "release" -> guiElement.onMouseUp(event);
          case "move" -> {
            boolean isOver = guiElementMouseOver.getOrDefault(guiElement, false);

            if (!isOver) {
              guiElementMouseOver.put(guiElement, true);
              guiElement.onMouseEnter(event);
            }
          }
        }
      } else {
        boolean isOver = guiElementMouseOver.getOrDefault(guiElement, false);

        if (isOver) {
          guiElement.onMouseExit(event);
          guiElementMouseOver.put(guiElement, false);
        }
      }
    });
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

  private record GuiElementPosition(GuiElement guiElement, int x, int y) {
  }
}
