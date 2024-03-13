package technology.sola.engine.server.rest;

import technology.sola.json.JsonObject;

import java.util.Map;

public class RequestParameters {
  private Map<String, String> pathParameters;
  private Map<String, String> queryParameters;
  private JsonObject body;
}
