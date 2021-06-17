package technology.sola.engine.tools.font;

import com.google.gson.Gson;
import technology.sola.engine.tools.ToolExecutable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    var font = new Font(fontName, fontStyle.getCode(), fontSize);
    FontInformation fontInformation = new FontInformation(font);

    List<FontGlyphModel> fontGlyphModelList = fontInformation.getFontGlyphs(CHARACTERS);
    Rectangle2D fullBounds = fontInformation.getStringBounds(CHARACTERS);
    int imageWidth = (int)fullBounds.getWidth() / 2;
    int imageHeight = (int)fullBounds.getHeight() * 5;

    FontCanvas fontCanvas = new FontCanvas(font, imageWidth, imageHeight);
    FontModel fontModel = new FontModel(
      fontName, fontStyle.name(), fontSize,
      fontInformation.getMaxAscent(), fontInformation.getLeading(),
      fontGlyphModelList
    );

    // TODO drawFontGlyphs currently mutates font glyph models
    fontCanvas.drawFontGlyphs(fontGlyphModelList, fontInformation.getMaxCharacterHeight());


    // TODO better way to handle this file io below here
    fontCanvas.saveToFile(new File(fontModel.getFile()));
    try {
      Files.write(Path.of(fontModel.getFile().replaceFirst(".png", ".json")), new Gson().toJson(fontModel).getBytes(StandardCharsets.UTF_8));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
