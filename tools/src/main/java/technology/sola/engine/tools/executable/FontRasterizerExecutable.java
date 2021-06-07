package technology.sola.engine.tools.executable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
  private enum FontStyle {
    NORMAL(0),
    BOLD(1),
    ITALIC(2),
    ;

    public static FontStyle valueOf(int code) {
      for (FontStyle fontStyle : values()) {
        if (fontStyle.code == code) {
          return fontStyle;
        }
      }

      throw new IllegalArgumentException("FontStyle code [" + code + "] is invalid");
    }

    private final int code;

    FontStyle(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }

  @Override
  public void execute(String[] toolArgs) {
    String fontName = "monospaced";
    FontStyle fontStyle = FontStyle.NORMAL;
    int fontSize = 16;

    String characters = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";


    // logic below here

    int imageWidth = fontSize * 10;
    int imageHeight = fontSize * characters.length() / 10;
    final int margin = 1;

    BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
    Font font = new Font(fontName, fontStyle.getCode(), fontSize);

    FontMetrics fontMetrics = graphics.getFontMetrics(font);
    graphics.setColor(Color.BLACK);
    graphics.setFont(font);

    int x = 0;
    int y = fontMetrics.getMaxAscent();

    JsonObject jsonFontInfo = new JsonObject();
    JsonArray jsonGlyphInfo = new JsonArray();


    for (String character : characters.split("")) {
      Rectangle2D rectangle = fontMetrics.getStringBounds(character, graphics);
      int characterWidth = (int) rectangle.getWidth();
      int characterHeight = fontMetrics.getAscent();

      if (x + characterWidth >= imageWidth) {
        x = 0;
        y += fontSize + margin;
      }

      JsonObject jsonGlyph = new JsonObject();

      jsonGlyph.addProperty("glyph", character);
      jsonGlyph.addProperty("x", x);
      jsonGlyph.addProperty("y", y);
      jsonGlyph.addProperty("width", characterWidth);
      jsonGlyph.addProperty("height", characterHeight);

      jsonGlyphInfo.add(jsonGlyph);

      graphics.drawString(character, x, y);

      x += characterWidth + margin;
    }

    graphics.dispose();

    String fontInfoFileName = fontName + "_" + fontStyle.name() + "_" + fontSize + ".json";
    String fontFileName = fontName + "_" + fontStyle.name() + "_" + fontSize + ".png";
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
