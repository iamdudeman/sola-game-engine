package technology.sola.engine.graphics.gui;

public class GuiElementGlobalProperties {
  private boolean isLayoutChanged = true;
  private String defaultFont;
  private String defaultTextColor;

  public GuiElementGlobalProperties(String defaultFont, String defaultTextColor) {
    this.defaultFont = defaultFont;
    this.defaultTextColor = defaultTextColor;
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public void setLayoutChanged(boolean layoutChanged) {
    isLayoutChanged = layoutChanged;
  }

  public String getDefaultFont() {
    return defaultFont;
  }

  public void setDefaultFont(String defaultFont) {
    this.defaultFont = defaultFont;
    setLayoutChanged(true);
  }

  public String getDefaultTextColor() {
    return defaultTextColor;
  }

  public void setDefaultTextColor(String defaultTextColor) {
    this.defaultTextColor = defaultTextColor;
  }
}
