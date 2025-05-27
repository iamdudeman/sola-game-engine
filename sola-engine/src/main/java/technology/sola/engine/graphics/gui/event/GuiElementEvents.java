package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;

/**
 * GuiElementEvents is a container for the {@link GuiEventListenerList}s for various event types
 * for {@link technology.sola.engine.graphics.gui.GuiElement}s.
 */
@NullMarked
public class GuiElementEvents {
  private final GuiEventListenerList<GuiKeyEvent> keyPressedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiKeyEvent> keyReleasedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mousePressedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseReleasedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseMovedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseEnteredEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseExitedEventListenerList = new GuiEventListenerList<>();

  /**
   * @return the {@link GuiEventListener} for key pressed events
   */
  public GuiEventListenerList<GuiKeyEvent> keyPressed() {
    return keyPressedEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for key released events
   */
  public GuiEventListenerList<GuiKeyEvent> keyReleased() {
    return keyReleasedEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for mouse pressed events
   */
  public GuiEventListenerList<GuiMouseEvent> mousePressed() {
    return mousePressedEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for mouse released events
   */
  public GuiEventListenerList<GuiMouseEvent> mouseReleased() {
    return mouseReleasedEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for mouse moved events
   */
  public GuiEventListenerList<GuiMouseEvent> mouseMoved() {
    return mouseMovedEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for mouse entered events
   */
  public GuiEventListenerList<GuiMouseEvent> mouseEntered() {
    return mouseEnteredEventListenerList;
  }

  /**
   * @return the {@link GuiEventListener} for mouse exited events
   */
  public GuiEventListenerList<GuiMouseEvent> mouseExited() {
    return mouseExitedEventListenerList;
  }
}
