package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.event.GuiEventList;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;

import java.util.List;

public abstract class GuiElement {
  private StyleContainer style;
  // todo when adding child be sure to remove from previous parent if there is one
  private List<GuiElement> children;
  private GuiElement parent;
  private boolean isLayoutChanged;
  private Rectangle boundingRect;
  private final GuiEventList<GuiKeyEvent> keyPressedEventList = new GuiEventList<>();

  public abstract void render(Renderer renderer);

  public void renderChildren(Renderer renderer) {
    // todo handle layout stuff and what not
  }

  public void recalculateLayout() {
    isLayoutChanged = true;
  }

  // onKeyPressed
  // oneKeyPressed
  // offKeyPressed

  // todo consider naming
  public GuiEventList<GuiKeyEvent> getKeyPressedEventList() {
    return keyPressedEventList;
  }

  void onKeyPressed(KeyEvent keyEvent) {
    GuiKeyEvent guiKeyEvent = new GuiKeyEvent(keyEvent);

    // todo check if in bounds

    // todo call event listeners
    keyPressedEventList.trigger(guiKeyEvent);

    // todo if propagation not stopped then propagate
  }

  void onKeyReleased(KeyEvent keyEvent) {
    // todo
  }

  void onMousePressed(MouseEvent event) {
    // todo
  }

  void onMouseReleased(MouseEvent event) {
    // todo
  }

  void onMouseMoved(MouseEvent event) {
    // todo
  }
}
