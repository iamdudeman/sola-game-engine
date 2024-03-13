package technology.sola.engine.server.rest;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

public record SolaResponse(int status, JsonElement body) {
  public SolaResponse(int status, JsonObject body) {
    this(status, new JsonElement(body));
  }

  public SolaResponse(int status, JsonArray body) {
    this(status, new JsonElement(body));
  }
}
