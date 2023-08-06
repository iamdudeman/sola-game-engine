package technology.sola.engine.graphics.gui.event;

import technology.sola.engine.input.KeyEvent;

public class GuiKeyEvent extends GuiEvent {
  private final KeyEvent keyEvent;
  private final Type type;

  public GuiKeyEvent(KeyEvent keyEvent, Type type) {
    this.keyEvent = keyEvent;
    this.type = type;
  }

  public int getKeyCode() {
    return keyEvent.keyCode();
  }

  public Type getType() {
    return type;
  }

  public enum Type {
    PRESS,
    RELEASE
  }
}
