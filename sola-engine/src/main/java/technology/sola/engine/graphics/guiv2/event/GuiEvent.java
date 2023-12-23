package technology.sola.engine.graphics.guiv2.event;

/**
 * GuiEvent defines the general api for gui specific events.
 */
public abstract class GuiEvent {
  private boolean isAbleToPropagate = true;
  private boolean isPreventingDefault = false;

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

  /**
   * @return whether default event handlers on the element should fire or not
   */
  public boolean isPreventingDefault() {
    return isPreventingDefault;
  }

  /**
   * Stops default event handlers from firing on the element.
   */
  public void preventDefault() {
    isPreventingDefault = true;
  }
}
