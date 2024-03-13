package technology.sola.engine.server.rest;

import com.sun.net.httpserver.HttpExchange;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SolaRouter {
  private final List<Route> routes = new ArrayList<>();

  public SolaRouter addRoute(String method, String path, RouteHandler routeHandler) {
    routes.add(new Route(method, path, routeHandler));

    return this;
  }

  public SolaRouter get(String path, RouteHandler routeHandler) {
    return addRoute("GET", path, routeHandler);
  }

  public JsonElement handleRequest(HttpExchange httpExchange) {
    // todo iterate through routes to find a match
    return new JsonElement(new JsonObject());
  }

  private record Route(String method, String path, RouteHandler routeHandler) {
  }
}
