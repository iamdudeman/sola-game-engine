package technology.sola.engine.graphics.guiv2.event;

public class GuiElementEvents {
  private final GuiEventListenerList<GuiKeyEvent> keyPressedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiKeyEvent> keyReleasedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mousePressedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseReleasedEventListenerList = new GuiEventListenerList<>();
  private final GuiEventListenerList<GuiMouseEvent> mouseMovedEventListenerList = new GuiEventListenerList<>();

  public GuiEventListenerList<GuiKeyEvent> keyPressed() {
    return keyPressedEventListenerList;
  }

  public GuiEventListenerList<GuiKeyEvent> keyReleased() {
    return keyReleasedEventListenerList;
  }

  public GuiEventListenerList<GuiMouseEvent> mousePressed() {
    return mousePressedEventListenerList;
  }

  public GuiEventListenerList<GuiMouseEvent> mouseReleased() {
    return mouseReleasedEventListenerList;
  }

  public GuiEventListenerList<GuiMouseEvent> mouseMoved() {
    return mouseMovedEventListenerList;
  }
}
