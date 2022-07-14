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

  private GuiLayoutDirection guiLayoutDirection = GuiLayoutDirection.HORIZONTAL;
  private GuiLayoutAnchor guiLayoutAnchor = GuiLayoutAnchor.TOP_LEFT;

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
    invalidate();
  }

  public void setDirection(GuiLayoutDirection guiLayoutDirection) {
    this.guiLayoutDirection = guiLayoutDirection;
    invalidate();
  }

  public void invalidate() {
    guiElementPositions.clear();

    if (guiLayoutDirection == GuiLayoutDirection.HORIZONTAL && guiLayoutAnchor == GuiLayoutAnchor.TOP_LEFT) {
      int xOffset = 0;

      for (GuiElement guiElement : guiElementList) {
        xOffset += guiElement.properties().margin.getLeft();

        guiElementPositions.add(new GuiElementPosition(guiElement, x + xOffset, y));

        int width = guiElement.getWidth();

        xOffset += width + guiElement.properties().margin.getRight();
      }
    } else if (guiLayoutDirection == GuiLayoutDirection.VERTICAL && guiLayoutAnchor == GuiLayoutAnchor.TOP_LEFT) {
      int yOffset = 0;

      for (GuiElement guiElement : guiElementList) {
        yOffset += guiElement.properties().margin.getTop();

        guiElementPositions.add(new GuiElementPosition(guiElement, x, y + yOffset));

        int height = guiElement.getHeight();

        yOffset += height + guiElement.properties().margin.getBottom();
      }
    } else {
      throw new RuntimeException("not yet implemented");
    }
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

  private record GuiElementPosition(GuiElement guiElement, int x, int y) {
  }
}
