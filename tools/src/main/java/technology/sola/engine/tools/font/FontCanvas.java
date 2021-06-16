package technology.sola.engine.tools.font;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontCanvas {
  private final BufferedImage bufferedImage;
  private final Graphics2D graphics2D;
  private final FontMetrics fontMetrics;

  public FontCanvas(Font font, int width, int height) {
    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setColor(Color.BLACK);
    graphics2D.setFont(font);

    fontMetrics = graphics2D.getFontMetrics();
  }

  public void drawString(String text, int x, int y) {
    graphics2D.drawString(text, x, y);
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

  public Rectangle2D getStringBounds(String string) {
    return fontMetrics.getStringBounds(string, graphics2D);
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
