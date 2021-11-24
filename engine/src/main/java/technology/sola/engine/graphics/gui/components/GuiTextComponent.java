package technology.sola.engine.graphics.gui.components;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.font.Font;

public class GuiTextComponent implements Component {
  private String fontAssetId;
  private String text;
  private Color color = Color.BLACK;
  private transient Font cachedFont;

  public GuiTextComponent() {
  }

  public GuiTextComponent(String fontAssetId, String text) {
    this.fontAssetId = fontAssetId;
    this.text = text;
  }

  public GuiTextComponent(String fontAssetId, String text, Color color) {
    this.fontAssetId = fontAssetId;
    this.text = text;
    this.color = color;
  }

  public Font getFont(AssetPool<Font> fontAssetPool) {
    if (cachedFont == null) {
      cachedFont = fontAssetPool.getAsset(fontAssetId);
    }

    return cachedFont;
  }

  public String getText() {
    return text;
  }

  public Color getColor() {
    return color;
  }

  public void setFontAssetId(String fontAssetId) {
    this.fontAssetId = fontAssetId;
    cachedFont = null;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
