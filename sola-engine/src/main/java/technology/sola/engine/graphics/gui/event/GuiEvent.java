package technology.sola.engine.graphics.gui.event;

import org.jspecify.annotations.NullMarked;

/**
 * GuiEvent defines the general api for gui specific events.
 */
@NullMarked
public abstract class GuiEvent {
  private boolean isAbleToPropagate = true;

  /**
   * Returns whether this event will propagate to child elements or not.
   *
   * @return true if the event will propagate to child elements
   */
  public boolean isAbleToPropagate() {
    return isAbleToPropagate;
  }

  /**
   * Stops this event from propagating further.
   */
  public void stopPropagation() {
    isAbleToPropagate = false;
  }
}
