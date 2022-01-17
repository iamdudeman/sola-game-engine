package technology.sola.engine.editor.tools.font;

import technology.sola.engine.graphics.font.FontGlyph;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class FontCanvas implements AutoCloseable {
  private final BufferedImage bufferedImage;
  private final Graphics2D graphics2D;
  private final FontInformation fontInformation;

  public FontCanvas(FontInformation fontInformation, int width, int height) {
    this.fontInformation = fontInformation;
    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setColor(Color.BLACK);
    graphics2D.setFont(fontInformation.getFont());
  }

  public java.util.List<FontGlyph> drawFontGlyphs(String characters) {
    var x = 0;
    var y = 0;

    java.util.List<FontGlyph> fontGlyphModelList = fontInformation.getFontGlyphs(characters);
    List<FontGlyph> fontGlyphModelsWithPosition = new ArrayList<>();

    for (FontGlyph fontGlyph : fontGlyphModelList) {
      int characterWidth = fontGlyph.width();

      if (x + characterWidth >= bufferedImage.getWidth()) {
        x = 0;
        y += fontInformation.getMaxCharacterHeight();
      }

      fontGlyphModelsWithPosition.add(new FontGlyph(fontGlyph.glyph(), x, y, fontGlyph.width(), fontGlyph.height()));
      drawString("" + fontGlyph.glyph(), x, y);

      x += characterWidth;
    }

    return fontGlyphModelsWithPosition;
  }

  public BufferedImage getBufferedImage() {
    return bufferedImage;
  }

  @Override
  public void close() {
    graphics2D.dispose();
  }

  private void drawString(String text, int x, int y) {
    graphics2D.drawString(text, x, y + fontInformation.getMaxAscent());
  }
}
