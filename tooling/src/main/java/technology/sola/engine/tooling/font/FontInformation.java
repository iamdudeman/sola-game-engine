package technology.sola.engine.tooling.font;

import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.engine.assets.graphics.font.FontStyle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class FontInformation {
  private final String fontFamily;
  private final String fontStyle;
  private final int fontSize;
  private final String fontFileName;
  private final String fontInfoFileName;
  private final Font font;
  private final FontMetrics fontMetrics;
  private final Graphics2D graphics2D;

  FontInformation(String fontFamily, FontStyle fontStyle, int fontSize) {
    this.fontFamily = fontFamily;
    this.fontStyle = fontStyle.name();
    this.fontSize = fontSize;
    this.fontFileName = fontFamily + "_" + fontStyle + "_" + fontSize + ".png";
    this.fontInfoFileName = fontFamily + "_" + fontStyle + "_" + fontSize + ".json";

    this.font = new Font(fontFamily, fontStyle.getCode(), fontSize);
    var bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setFont(font);

    fontMetrics = graphics2D.getFontMetrics();
  }

  Rectangle2D getStringBounds(String string) {
    return fontMetrics.getStringBounds(string, graphics2D);
  }

  List<FontGlyph> getFontGlyphs(String characters) {
    List<FontGlyph> fontGlyphList = new ArrayList<>(characters.length());

    for (char character : characters.toCharArray()) {
      Rectangle2D characterBounds = getStringBounds("" + character);
      int width = (int) characterBounds.getWidth();
      int height = (int) characterBounds.getHeight();

      fontGlyphList.add(new FontGlyph(character, -1, -1, width, height));
    }

    return fontGlyphList;
  }

  String getFontFamily() {
    return fontFamily;
  }

  String getFontStyle() {
    return fontStyle;
  }

  int getFontSize() {
    return fontSize;
  }

  String getFontFileName() {
    return fontFileName;
  }

  String getFontInfoFileName() {
    return fontInfoFileName;
  }

  int getMaxAscent() {
    return fontMetrics.getMaxAscent();
  }

  int getLeading() {
    return fontMetrics.getLeading();
  }

  int getMaxCharacterHeight() {
    return fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
  }

  Font getFont() {
    return font;
  }
}
