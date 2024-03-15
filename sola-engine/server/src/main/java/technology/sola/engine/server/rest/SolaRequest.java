package technology.sola.engine.server.rest;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.Map;

/**
 * SolaRequest contains information parsed out from an {@link com.sun.net.httpserver.HttpExchange} that is being handled
 * by {@link technology.sola.engine.server.SolaServer}.
 *
 * @param pathParameters  the path parameters from the request
 * @param queryParameters the query parameters from the request
 * @param body            the {@link JsonElement} body from the request
 */
public record SolaRequest(
  Map<String, String> pathParameters,
  Map<String, String> queryParameters,
  JsonElement body
) {
  /**
   * SolaRequest contains information parsed out from an {@link com.sun.net.httpserver.HttpExchange} that is being handled
   * by {@link technology.sola.engine.server.SolaServer}.
   *
   * @param pathParameters  the path parameters from the request
   * @param queryParameters the query parameters from the request
   * @param body            the {@link JsonObject} body from the request
   */
  public SolaRequest(
    Map<String, String> pathParameters,
    Map<String, String> queryParameters,
    JsonObject body
  ) {
    this(pathParameters, queryParameters, new JsonElement(body));
  }

  /**
   * SolaRequest contains information parsed out from an {@link com.sun.net.httpserver.HttpExchange} that is being handled
   * by {@link technology.sola.engine.server.SolaServer}.
   *
   * @param pathParameters  the path parameters from the request
   * @param queryParameters the query parameters from the request
   * @param body            the {@link JsonArray} body from the request
   */
  public SolaRequest(
    Map<String, String> pathParameters,
    Map<String, String> queryParameters,
    JsonArray body
  ) {
    this(pathParameters, queryParameters, new JsonElement(body));
  }
}
