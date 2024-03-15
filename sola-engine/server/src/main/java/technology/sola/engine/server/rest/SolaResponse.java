package technology.sola.engine.server.rest;

import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

/**
 * SolaResponse defines the contract for {@link technology.sola.engine.server.SolaServer} after an
 * {@link com.sun.net.httpserver.HttpExchange} has been handled.
 *
 * @param status the status of the response
 * @param body   the {@link JsonElement} body of the response
 */
public record SolaResponse(int status, JsonElement body) {
  /**
   * SolaResponse defines the contract for {@link technology.sola.engine.server.SolaServer} after an
   * {@link com.sun.net.httpserver.HttpExchange} has been handled.
   *
   * @param status the status of the response
   * @param body   the {@link JsonObject} body of the response
   */
  public SolaResponse(int status, JsonObject body) {
    this(status, new JsonElement(body));
  }

  /**
   * SolaResponse defines the contract for {@link technology.sola.engine.server.SolaServer} after an
   * {@link com.sun.net.httpserver.HttpExchange} has been handled.
   *
   * @param status the status of the response
   * @param body   the {@link JsonArray} body of the response
   */
  public SolaResponse(int status, JsonArray body) {
    this(status, new JsonElement(body));
  }
}
