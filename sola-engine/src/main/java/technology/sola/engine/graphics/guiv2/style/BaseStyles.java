package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;

// todo how does updating this trigger a change in GuiElement for layout shift?
// todo ideally this is "stateless" outside of its own properties
// todo maybe StyleContainer#setStyles causes the change and BaseStyles as a whole has no setters (Builder only pattern)?

public class BaseStyles {
  private Color backgroundColor;
  // margin (layout)
  // padding (layout)
  // focus outline (layout?)
  // display (layout)
  // borderWidth (layout)
  // borderColor

  public BaseStyles setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
