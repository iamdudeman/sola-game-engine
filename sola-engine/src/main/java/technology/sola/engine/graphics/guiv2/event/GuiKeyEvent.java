package technology.sola.engine.graphics.guiv2.event;

import technology.sola.engine.input.KeyEvent;

public class GuiKeyEvent extends GuiEvent {
  private final KeyEvent keyEvent;

  public GuiKeyEvent(KeyEvent keyEvent) {
    this.keyEvent = keyEvent;
  }

  public KeyEvent getKeyEvent() {
    return keyEvent;
  }
}
