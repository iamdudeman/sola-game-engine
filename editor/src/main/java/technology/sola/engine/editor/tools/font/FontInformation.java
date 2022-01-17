package technology.sola.engine.editor.tools.font;

import technology.sola.engine.graphics.font.FontGlyph;
import technology.sola.engine.graphics.font.FontStyle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class FontInformation {
  private final String fontName;
  private final String fontStyle;
  private final int fontSize;
  private final String fontFileName;
  private final String fontInfoFileName;
  private final Font font;
  private final FontMetrics fontMetrics;
  private final Graphics2D graphics2D;

  public FontInformation(String fontName, FontStyle fontStyle, int fontSize) {
    this.fontName = fontName;
    this.fontStyle = fontStyle.name();
    this.fontSize = fontSize;
    this.fontFileName = fontName + "_" + fontStyle + "_" + fontSize + ".png";
    this.fontInfoFileName = fontName + "_" + fontStyle + "_" + fontSize + ".json";

    this.font = new Font(fontName, fontStyle.getCode(), fontSize);
    var bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setFont(font);

    fontMetrics = graphics2D.getFontMetrics();
  }

  public Rectangle2D getStringBounds(String string) {
    return fontMetrics.getStringBounds(string, graphics2D);
  }

  public java.util.List<FontGlyph> getFontGlyphs(String characters) {
    List<FontGlyph> fontGlyphList = new ArrayList<>(characters.length());

    for (char character : characters.toCharArray()) {
      Rectangle2D characterBounds = getStringBounds("" + character);
      int width = (int) characterBounds.getWidth();
      int height = (int) characterBounds.getHeight();

      fontGlyphList.add(new FontGlyph(character, -1, -1, width, height));
    }

    return fontGlyphList;
  }

  public String getFontName() {
    return fontName;
  }

  public String getFontStyle() {
    return fontStyle;
  }

  public int getFontSize() {
    return fontSize;
  }

  public String getFontFileName() {
    return fontFileName;
  }

  public String getFontInfoFileName() {
    return fontInfoFileName;
  }

  public int getMaxAscent() {
    return fontMetrics.getMaxAscent();
  }

  public int getLeading() {
    return fontMetrics.getLeading();
  }

  public int getMaxCharacterHeight() {
    return fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
  }

  public Font getFont() {
    return font;
  }
}
