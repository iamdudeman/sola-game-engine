package technology.sola.engine.tools.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import technology.sola.engine.tools.ToolExecutable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// todo for cleanup
// identify font
// build font glyph info for characters
// build canvas for size
// render font glyphs to image
// save font info to json
// save glyph image
// done

public class FontRasterizerExecutable implements ToolExecutable {
  private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";

  public static void main(String[] args) {
    // TODO only for testing
    new FontRasterizerExecutable().execute();
  }

  @Override
  public void execute(String... toolArgs) {
    // TODO read some stuff form toolArgs instead
    String fontName = "monospaced";
    FontStyle fontStyle = FontStyle.NORMAL;
    int fontSize = 18;



    // logic below here

    var font = new Font(fontName, fontStyle.getCode(), fontSize);
    int imageWidth = fontSize * CHARACTERS.length() / 5;
    int imageHeight = fontSize * CHARACTERS.length() / 10;

    FontCanvas fontCanvas = new FontCanvas(font, imageWidth, imageHeight);

    int x = 0;
    int y = fontCanvas.getMaxAscent();

    JsonArray jsonGlyphInfo = new JsonArray();

    for (String character : CHARACTERS.split("")) {
      Rectangle2D rectangle = fontCanvas.getStringBounds(character);
      int characterWidth = (int) rectangle.getWidth();
      int characterHeight = (int) rectangle.getHeight();

      if (x + characterWidth >= imageWidth) {
        x = 0;
        y += fontCanvas.getMaxCharacterHeight();
      }

      JsonObject jsonGlyph = new JsonObject();
      jsonGlyph.addProperty("glyph", character);
      jsonGlyph.addProperty("x", x);
      jsonGlyph.addProperty("y", y - fontCanvas.getMaxAscent());
      jsonGlyph.addProperty("width", characterWidth);
      jsonGlyph.addProperty("height", characterHeight);
      jsonGlyphInfo.add(jsonGlyph);

      fontCanvas.drawString(character, x, y);

      x += characterWidth;
    }

    String baseFontName = fontName + "_" + fontStyle.name() + "_" + fontSize;
    String fontInfoFileName = baseFontName + ".json";
    String fontFileName = baseFontName + ".png";

    JsonObject jsonFontInfo = new JsonObject();
    jsonFontInfo.addProperty("file", fontFileName);
    jsonFontInfo.addProperty("font", fontName);
    jsonFontInfo.addProperty("fontStyle", fontStyle.name());
    jsonFontInfo.addProperty("fontSize", fontSize);
    jsonFontInfo.addProperty("maxAscent", fontCanvas.getMaxAscent());
    jsonFontInfo.addProperty("leading", fontCanvas.getLeading());
    jsonFontInfo.add("glyphs", jsonGlyphInfo);

    File file = new File(fontFileName);

    fontCanvas.saveToFile(file);

    try {
      Files.write(Path.of(fontInfoFileName), jsonFontInfo.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
