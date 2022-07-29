package technology.sola.engine.graphics.gui.event;

import technology.sola.engine.input.KeyEvent;

public class GuiKeyEvent {
  private final KeyEvent keyEvent;
  private final Type type;
  private boolean isAbleToPropagate = true;

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

  public boolean isAbleToPropagate() {
    return isAbleToPropagate;
  }

  public void stopPropagation() {
    isAbleToPropagate = false;
  }

  public enum Type {
    PRESS,
    RELEASE
  }
}
