package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.input.MouseEvent;

// TODO consider ability to add a sub GuiPanel?

public class GuiPanel {
  private int x;
  private int y;
  private GuiElement<?> root;

  public GuiPanel(int x, int y, GuiElement<?> root) {
    this.x = x;
    this.y = y;
    this.root = root;
  }

  public void render(Renderer renderer) {
    root.render(renderer, x, y);
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
//    guiElementPositions.forEach(guiElementPosition -> {
//      GuiElement guiElement = guiElementPosition.guiElement;
//
//      if (guiElement.properties().isHidden()) {
//        return;
//      }
//
//      Integer x = guiElementPosition.x;
//      Integer y = guiElementPosition.y;
//      Integer width = guiElement.getWidth();
//      Integer height = guiElement.getHeight();
//
//      Rectangle guiElementBounds = new Rectangle(new Vector2D(x, y), new Vector2D(x + width, y + height));
//
//      if (guiElementBounds.contains(new Vector2D(event.x(), event.y()))) {
//        switch (eventType) {
//          case "press" -> guiElement.onMouseDown(event);
//          case "release" -> guiElement.onMouseUp(event);
//          case "move" -> {
//            boolean isOver = guiElementMouseOver.getOrDefault(guiElement, false);
//
//            if (!isOver) {
//              guiElementMouseOver.put(guiElement, true);
//              guiElement.onMouseEnter(event);
//            }
//          }
//        }
//      } else {
//        boolean isOver = guiElementMouseOver.getOrDefault(guiElement, false);
//
//        if (isOver) {
//          guiElement.onMouseExit(event);
//          guiElementMouseOver.put(guiElement, false);
//        }
//      }
//    });
  }
}
