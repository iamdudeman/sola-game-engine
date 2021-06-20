package technology.sola.engine.tools.font;

import com.google.gson.Gson;
import technology.sola.engine.tools.ToolExecutable;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FontRasterizerExecutable implements ToolExecutable {
  private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";

  public static void main(String[] args) {
    var fontsToCreate = List.of(
      new String[] {"monospaced", FontStyle.NORMAL.name(), "18" },
      new String[] {"times", FontStyle.NORMAL.name(), "18" },
      new String[] {"monospaced", FontStyle.NORMAL.name(), "12" }
    );
    var fontRasterizerExecutable = new FontRasterizerExecutable();

    fontsToCreate.forEach(fontRasterizerExecutable::execute);
  }

  @Override
  public void execute(String... toolArgs) {
    var fontInformation = prepareFontInformation(toolArgs);

    try (var fontCanvas = prepareFontCanvas(fontInformation)) {
      var fontModel = prepareFontModel(fontInformation, fontCanvas);

      fontCanvas.saveToFile(fontInformation.getFontFileName());
      Files.write(Path.of(fontInformation.getFontInfoFileName()), serializeFontModel(fontModel));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private FontInformation prepareFontInformation(String... toolArgs) {
    var fontName = toolArgs[0];
    var fontStyle = FontStyle.valueOf(toolArgs[1]);
    var fontSize = Integer.parseInt(toolArgs[2]);

    return new FontInformation(fontName, fontStyle, fontSize);
  }

  private FontCanvas prepareFontCanvas(FontInformation fontInformation) {
    Rectangle2D fullBounds = fontInformation.getStringBounds(CHARACTERS);
    int imageWidth = (int)fullBounds.getWidth() / 2;
    int imageHeight = (int)fullBounds.getHeight() * 5;

    return new FontCanvas(fontInformation, imageWidth, imageHeight);
  }

  private FontModel prepareFontModel(FontInformation fontInformation, FontCanvas fontCanvas) {
    List<FontGlyphModel> fontGlyphModelList = fontInformation.getFontGlyphs(CHARACTERS);
    List<FontGlyphModel> fontGlyphModelsWithPositions = fontCanvas.drawFontGlyphs(fontGlyphModelList, fontInformation.getMaxCharacterHeight());

    return new FontModel(fontInformation, fontGlyphModelsWithPositions);
  }

  private byte[] serializeFontModel(FontModel fontModel) {
    return new Gson().toJson(fontModel).getBytes(StandardCharsets.UTF_8);
  }
}
