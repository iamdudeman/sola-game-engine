package technology.sola.engine.assets.graphics.font.mapper;

import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

public class FontInfoJsonMapper implements JsonMapper<FontInfo> {
  private final FontGlyphJsonMapper fontGlyphJsonMapper = new FontGlyphJsonMapper();

  @Override
  public Class<FontInfo> getObjectClass() {
    return FontInfo.class;
  }

  @Override
  public JsonObject toJson(FontInfo fontInfo) {
    JsonArray glyphs = fontGlyphJsonMapper.toJson(fontInfo.glyphs());
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("fontGlyphFile", fontInfo.fontGlyphFile());
    jsonObject.put("fontName", fontInfo.fontName());
    jsonObject.put("fontStyle", fontInfo.fontStyle().name());
    jsonObject.put("fontSize", fontInfo.fontSize());
    jsonObject.put("leading", fontInfo.leading());
    jsonObject.put("glyphs", glyphs);

    return jsonObject;
  }

  @Override
  public FontInfo toObject(JsonObject jsonObject) {
    List<FontGlyph> glyphs = jsonObject.getArray("glyphs")
      .stream().map(jsonGlyph -> fontGlyphJsonMapper.toObject(jsonGlyph.asObject()))
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
}
