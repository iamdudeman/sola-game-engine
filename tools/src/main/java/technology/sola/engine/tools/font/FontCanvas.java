package technology.sola.engine.tools.font;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FontCanvas implements AutoCloseable {
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

  public List<FontGlyphModel> drawFontGlyphs(String characters) {
    var x = 0;
    var y = 0;

    List<FontGlyphModel> fontGlyphModelList = fontInformation.getFontGlyphs(characters);
    List<FontGlyphModel> fontGlyphModelsWithPosition = new ArrayList<>();

    for (FontGlyphModel character : fontGlyphModelList) {
      int characterWidth = character.getWidth();

      if (x + characterWidth >= bufferedImage.getWidth()) {
        x = 0;
        y += fontInformation.getMaxCharacterHeight();
      }

      fontGlyphModelsWithPosition.add(new FontGlyphModel(character, x, y));
      drawString(character.getGlyph(), x, y);

      x += characterWidth;
    }

    return fontGlyphModelsWithPosition;
  }

  public void saveToFile(String filename) throws IOException {
    ImageIO.write(bufferedImage, "png", new File(filename));
  }

  @Override
  public void close() {
    graphics2D.dispose();
  }

  private void drawString(String text, int x, int y) {
    graphics2D.drawString(text, x, y + fontInformation.getMaxAscent());
  }
}
