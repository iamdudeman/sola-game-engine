package technology.sola.engine.graphics.font;

import technology.sola.json.JsonArray;
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
      JsonArray glyphs = FontGlyph.JSON_MAPPER.toJson(fontInfo.glyphs);
      JsonObject jsonObject = new JsonObject();

      jsonObject.put("fontGlyphFile", fontInfo.fontGlyphFile);
      jsonObject.put("fontName", fontInfo.fontName);
      jsonObject.put("fontStyle", fontInfo.fontStyle.name());
      jsonObject.put("fontSize", fontInfo.fontSize);
      jsonObject.put("leading", fontInfo.leading);
      jsonObject.put("glyphs", glyphs);

      return jsonObject;
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
