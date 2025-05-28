package technology.sola.engine.assets.graphics.spritesheet;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * SpriteSheetInfoJsonMapper is a {@link JsonMapper} implementation for {@link SpriteSheetInfo}.
 */
@NullMarked
public class SpriteSheetInfoJsonMapper implements JsonMapper<SpriteSheetInfo> {
  @Override
  public Class<SpriteSheetInfo> getObjectClass() {
    return SpriteSheetInfo.class;
  }

  @Override
  public JsonObject toJson(SpriteSheetInfo spriteSheetInfo) {
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("spriteSheet", spriteSheetInfo.spriteSheet());

    JsonArray jsonArray = new JsonArray();

    spriteSheetInfo.sprites().forEach(sprite -> {
      JsonObject spriteJson = new JsonObject();

      spriteJson.put("id", sprite.id());
      spriteJson.put("x", sprite.x());
      spriteJson.put("y", sprite.y());
      spriteJson.put("w", sprite.width());
      spriteJson.put("h", sprite.height());

      jsonArray.add(spriteJson);
    });

    jsonObject.put("sprites", jsonArray);

    return jsonObject;
  }

  @Override
  public SpriteSheetInfo toObject(JsonObject jsonObject) {
    String spriteImageName = jsonObject.getString("spriteSheet");

    List<SpriteInfo> sprites = new ArrayList<>();

    jsonObject.getArray("sprites").forEach(jsonElement -> {
      var spriteJson = jsonElement.asObject();

      sprites.add(new SpriteInfo(
        spriteJson.getString("id"),
        spriteJson.getInt("x"),
        spriteJson.getInt("y"),
        spriteJson.getInt("w"),
        spriteJson.getInt("h")
      ));
    });

    return new SpriteSheetInfo(
      spriteImageName,
      sprites
    );
  }
}
