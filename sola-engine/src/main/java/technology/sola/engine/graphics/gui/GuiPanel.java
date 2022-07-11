package technology.sola.engine.graphics.gui;

// TODO study https://github.com/iamdudeman/sol2d/blob/master/src/main/java/me/solhub/sol2d/core/gamegui/GameGuiPanel.java

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiDocument;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated use {@link GuiDocument} once built
 */
@Deprecated
public class GuiPanel {
  private final List<GuiElementLocationDescription> guiElementLocationDescriptions;
  private final List<GuiElementPosition> guiElementPositions;

  private boolean isShowing = true;

  private Integer x;
  private Integer y;
  private Integer width;
  private Integer height;

  public GuiPanel(Integer x, Integer y, Integer width, Integer height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    guiElementLocationDescriptions = new ArrayList<>();
    guiElementPositions = new ArrayList<>();
  }


  public Integer getX() { return x; }


  public void setX(Integer x) {
    this.x = x;
    recalculatePositions();
  }


  public Integer getY() { return y; }


  public void setY(Integer y) {
    this.y = y;
    recalculatePositions();
  }


  public Integer getWidth() { return width; }


  public void setWidth(Integer width) {
    this.width = width;
    recalculatePositions();
  }

  public Integer getHeight() { return height; }


  public void setHeight(Integer height) {
    this.height = height;
    recalculatePositions();
  }

  public boolean isShowing() {
    return isShowing;
  }

  public void show() {
    isShowing = true;
  }

  public void hide() {
    isShowing = false;
  }

  public void addGuiElement(GuiElement guiElement, GuiElementAnchor anchor, float percentPositionX, float percentPositionY) {
    guiElementLocationDescriptions.add(new GuiElementLocationDescription(guiElement, anchor, percentPositionX, percentPositionY));
    recalculatePositions();
  }


  public void render(Renderer renderer) {
    if (isShowing) {
      guiElementPositions.stream()
        .filter(guiElementPosition -> guiElementPosition.guiElement.isShowing())
        .forEach(guiElementPosition -> guiElementPosition.guiElement.render(renderer, guiElementPosition.x, guiElementPosition.y));
    }
  }

  void handleMouseEvent(MouseEvent event, String eventType) {
    if (!isShowing()) {
      return;
    }

    guiElementPositions.forEach(guiElementPosition -> {
      GuiElement guiElement = guiElementPosition.guiElement;

      if (!guiElement.isShowing()) {
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

  private final Map<GuiElement, Boolean> guiElementMouseOver = new HashMap<>();


  private void recalculatePositions() {
    guiElementPositions.clear();
    guiElementLocationDescriptions.forEach(guiElementLocationDescription -> {
      GuiElement gameGuiComponent = guiElementLocationDescription.guiElement;
      GuiElementAnchor gameGuiPanelAnchor = guiElementLocationDescription.anchor;
      float percentPositionX = guiElementLocationDescription.percentPositionX;
      float percentPositionY = guiElementLocationDescription.percentPositionY;

      float availableWidth = width - gameGuiComponent.getWidth();
      float availableHeight = height - gameGuiComponent.getHeight();

      // Calculate x
      Integer gameGuiComponentX = switch (gameGuiPanelAnchor) {
        case CENTER -> Math.round(x + availableWidth / 2);
        case BOTTOM_RIGHT, TOP_RIGHT -> Math.round(x + (availableWidth * ((100 - percentPositionX) / 100)));
        case TOP_LEFT, BOTTOM_LEFT -> Math.round(x + (availableWidth * (percentPositionX / 100)));
      };

      // Calculate y
      Integer gameGuiComponentY = switch (gameGuiPanelAnchor) {
        case CENTER -> Math.round(y + availableHeight / 2);
        case TOP_LEFT, TOP_RIGHT -> Math.round(y + (availableHeight * (percentPositionY / 100)));
        case BOTTOM_RIGHT, BOTTOM_LEFT -> Math.round(y + (availableHeight * ((100 - percentPositionY) / 100)));
      };

      guiElementPositions.add(new GuiElementPosition(gameGuiComponent, gameGuiComponentX, gameGuiComponentY));
    });
  }

  private record GuiElementLocationDescription(
    GuiElement guiElement, GuiElementAnchor anchor, float percentPositionX, float percentPositionY) {
  }

  private record GuiElementPosition(GuiElement guiElement, int x, int y) {
  }
}
