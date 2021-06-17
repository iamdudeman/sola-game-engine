package technology.sola.engine.tools.font;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FontInformation {
  private final FontMetrics fontMetrics;
  private final Graphics2D graphics2D;

  public FontInformation(Font font) {
    var bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

    graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setFont(font);

    fontMetrics = graphics2D.getFontMetrics();
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

  public java.util.List<FontGlyphModel> getFontGlyphs(String characters) {
    List<FontGlyphModel> fontGlyphModelList = new ArrayList<>(characters.length());

    for (String character : characters.split("")) {
      Rectangle2D characterBounds = getStringBounds(character);
      int width = (int) characterBounds.getWidth();
      int height = (int) characterBounds.getHeight();

      fontGlyphModelList.add(new FontGlyphModel(character, width, height));
    }

    return fontGlyphModelList;
  }
}
