package technology.sola.engine.server.rest;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.Map;

public record RequestParameters(
  Map<String, String> pathParameters,
  Map<String, String> queryParameters,
  JsonElement body
) {
  public RequestParameters(
    Map<String, String> pathParameters,
    Map<String, String> queryParameters,
    JsonObject body
  ) {
    this(pathParameters, queryParameters, new JsonElement(body));
  }

  public RequestParameters(
    Map<String, String> pathParameters,
    Map<String, String> queryParameters,
    JsonArray body
  ) {
    this(pathParameters, queryParameters, new JsonElement(body));
  }
}
