package technology.sola.engine.graphics.font;

import technology.sola.json.JsonMapper;
import technology.sola.json.JsonObject;

public record FontGlyph(char glyph, int x, int y, int width, int height) {
  public static final JsonMapper<FontGlyph> JSON_MAPPER = new JsonMapper<>() {
    @Override
    public JsonObject toJson(FontGlyph fontGlyph) {
      JsonObject jsonObject = new JsonObject();

      jsonObject.put("glyph", "" + fontGlyph.glyph);
      jsonObject.put("x", fontGlyph.x);
      jsonObject.put("y", fontGlyph.y);
      jsonObject.put("width", fontGlyph.width);
      jsonObject.put("height", fontGlyph.height);

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
  };
}
