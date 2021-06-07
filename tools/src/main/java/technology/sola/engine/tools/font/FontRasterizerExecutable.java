package technology.sola.engine.tools.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import technology.sola.engine.tools.ToolExecutable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FontRasterizerExecutable implements ToolExecutable {
  private static final String CHARACTERS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
  private static final int CHARACTER_MARGIN = 1;

  @Override
  public void execute(String[] toolArgs) {
    // TODO read some stuff form toolArgs instead
    String fontName = "monospaced";
    FontStyle fontStyle = FontStyle.NORMAL;
    int fontSize = 16;



    // logic below here

    int imageWidth = fontSize * 10;
    int imageHeight = fontSize * CHARACTERS.length() / 10;

    var bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    var graphics = (Graphics2D) bufferedImage.getGraphics();
    var font = new Font(fontName, fontStyle.getCode(), fontSize);

    graphics.setColor(Color.BLACK);
    graphics.setFont(font);

    var fontMetrics = graphics.getFontMetrics();


    int x = 0;
    int y = fontMetrics.getMaxAscent();

    JsonArray jsonGlyphInfo = new JsonArray();

    for (String character : CHARACTERS.split("")) {
      Rectangle2D rectangle = fontMetrics.getStringBounds(character, graphics);
      int characterWidth = (int) rectangle.getWidth();
      int characterHeight = fontMetrics.getAscent();

      if (x + characterWidth >= imageWidth) {
        x = 0;
        y += fontSize + CHARACTER_MARGIN;
      }

      JsonObject jsonGlyph = new JsonObject();
      jsonGlyph.addProperty("glyph", character);
      jsonGlyph.addProperty("x", x);
      jsonGlyph.addProperty("y", y);
      jsonGlyph.addProperty("width", characterWidth);
      jsonGlyph.addProperty("height", characterHeight);
      jsonGlyphInfo.add(jsonGlyph);

      graphics.drawString(character, x, y);

      x += characterWidth + CHARACTER_MARGIN;
    }

    graphics.dispose();

    String baseFontName = fontName + "_" + fontStyle.name() + "_" + fontSize;
    String fontInfoFileName = baseFontName + ".json";
    String fontFileName = baseFontName + ".png";

    JsonObject jsonFontInfo = new JsonObject();
    jsonFontInfo.addProperty("file", fontFileName);
    jsonFontInfo.addProperty("font", fontName);
    jsonFontInfo.addProperty("fontStyle", fontStyle.name());
    jsonFontInfo.addProperty("fontSize", fontSize);
    jsonFontInfo.add("glyphs", jsonGlyphInfo);

    File file = new File(fontFileName);

    try {
      ImageIO.write(bufferedImage, "png", file);
      Files.write(Path.of(fontInfoFileName), jsonFontInfo.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
