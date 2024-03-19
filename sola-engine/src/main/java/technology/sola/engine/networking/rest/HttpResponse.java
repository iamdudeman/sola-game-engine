package technology.sola.engine.networking.rest;

import technology.sola.json.JsonElement;

public record HttpResponse(
  int status,
  JsonElement body
) {
}
