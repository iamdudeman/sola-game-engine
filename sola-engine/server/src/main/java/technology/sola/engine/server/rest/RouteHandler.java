package technology.sola.engine.server.rest;

import technology.sola.json.JsonObject;

@FunctionalInterface
public interface RouteHandler {
  SolaResponse handleRoute(RequestParameters requestParameters);
}
