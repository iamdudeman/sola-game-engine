package technology.sola.engine.assets.graphics.font.mapper;

import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

/**
 * FontGlyphJsonMapper is a {@link JsonMapper} implementation for {@link FontGlyph}s.
 */
public class FontGlyphJsonMapper implements JsonMapper<FontGlyph> {
  @Override
  public Class<FontGlyph> getObjectClass() {
    return FontGlyph.class;
  }

  @Override
  public JsonObject toJson(FontGlyph fontGlyph) {
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("glyph", "" + fontGlyph.glyph());
    jsonObject.put("x", fontGlyph.x());
    jsonObject.put("y", fontGlyph.y());
    jsonObject.put("width", fontGlyph.width());
    jsonObject.put("height", fontGlyph.height());

    return jsonObject;
  }

  @Override
  public FontGlyph toObject(JsonObject jsonObject) {
    return new FontGlyph(
      jsonObject.getString("glyph").charAt(0),
      jsonObject.getInt("x"),
      jsonObject.getInt("y"),
      jsonObject.getInt("width"),
      jsonObject.getInt("height")
    );
  }
}
