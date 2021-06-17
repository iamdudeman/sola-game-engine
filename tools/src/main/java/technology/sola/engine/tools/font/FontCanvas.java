package technology.sola.engine.tools.font;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FontCanvas {
  private BufferedImage bufferedImage;
  private Graphics2D graphics2D;
  private FontMetrics fontMetrics;

  public FontCanvas(Font font, int width, int height) {
    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setColor(Color.BLACK);
    graphics2D.setFont(font);

    fontMetrics = graphics2D.getFontMetrics();
  }

  public void drawFontGlyphs(List<FontGlyphModel> fontGlyphModelList, int maxCharacterHeight) {
    int x = 0;
    int y = 0;

    for (FontGlyphModel character : fontGlyphModelList) {
      int characterWidth = character.getWidth();

      if (x + characterWidth >= bufferedImage.getWidth()) {
        x = 0;
        y += maxCharacterHeight;
      }

      character.setX(x);
      character.setY(y);

      drawString(character.getGlyph(), x, y);

      x += characterWidth;
    }
  }

  public void drawString(String text, int x, int y) {
    graphics2D.drawString(text, x, y + fontMetrics.getMaxAscent());
  }

  public void saveToFile(File file) {
    try {
      graphics2D.dispose();
      ImageIO.write(bufferedImage, "png", file);
    } catch (IOException ex) {
      ex.printStackTrace();
      // todo specific exception
      throw new RuntimeException(ex);
    }
  }
}
