package technology.sola.engine.graphics.font;

import technology.sola.json.JsonMapper;
import technology.sola.json.JsonObject;

import java.util.List;

public record FontInfo(
  String fontGlyphFile, String fontName,
  FontStyle fontStyle, int fontSize, int leading,
  List<FontGlyph> glyphs) {

  public static final JsonMapper<FontInfo> JSON_MAPPER = new JsonMapper<>() {
    @Override
    public JsonObject toJson(FontInfo fontInfo) {
      throw new UnsupportedOperationException();
    }

    @Override
    public FontInfo toObject(JsonObject jsonObject) {
      List<FontGlyph> glyphs = jsonObject.getArray("glyphs")
        .stream().map(jsonGlyph -> FontGlyph.JSON_MAPPER.toObject(jsonGlyph.asObject()))
        .toList();

      return new FontInfo(
        jsonObject.getString("fontGlyphFile"),
        jsonObject.getString("fontName"),
        FontStyle.valueOf(jsonObject.getString("fontStyle")),
        jsonObject.getInt("fontSize"),
        jsonObject.getInt("leading"),
        glyphs
      );
    }
  };
}
