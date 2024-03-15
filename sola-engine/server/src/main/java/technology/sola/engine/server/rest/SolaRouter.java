package technology.sola.engine.server.rest;

import com.sun.net.httpserver.HttpExchange;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.util.*;

public class SolaRouter {
  private final List<Route> routes = new ArrayList<>();

  public SolaRouter route(String method, String path, RouteHandler routeHandler) {
    routes.add(new Route(method, path, routeHandler));

    return this;
  }

  public SolaRouter get(String path, RouteHandler routeHandler) {
    return route("GET", path, routeHandler);
  }

  public SolaRouter post(String path, RouteHandler routeHandler) {
    return route("POST", path, routeHandler);
  }

  public SolaRouter put(String path, RouteHandler routeHandler) {
    return route("PUT", path, routeHandler);
  }

  public SolaRouter delete(String path, RouteHandler routeHandler) {
    return route("DELETE", path, routeHandler);
  }

  public SolaResponse handleHttpExchange(HttpExchange httpExchange) {
    return routes.stream()
      .filter(route -> isRouteMatch(route, httpExchange))
      .findFirst()
      .map(route -> route.routeHandler)
      .orElse(solaRequest -> new SolaResponse(404, new JsonObject()))
      .handleRoute(buildRequestParameters(httpExchange));
  }

  private boolean isRouteMatch(Route route, HttpExchange exchange) {
    String method = exchange.getRequestMethod();
    String path = exchange.getRequestURI().getPath();

    // todo handle path param check later
    return route.method.equalsIgnoreCase(method) && route.path.equalsIgnoreCase(path);
  }

  private SolaRequest buildRequestParameters(HttpExchange exchange) {
    return new SolaRequest(
      parsePathParams(exchange),
      parseQueryParams(exchange),
      parseBody(exchange)
    );
  }

  private Map<String, String> parsePathParams(HttpExchange exchange) {
    // todo implement path params
    String path = exchange.getRequestURI().getPath();
    Map<String, String> pathParameters = new HashMap<>();

    return pathParameters;
  }

  private Map<String, String> parseQueryParams(HttpExchange exchange) {
    String query = exchange.getRequestURI().getQuery();
    Map<String, String> queryParameters = new HashMap<>();

    if (query != null) {
      var queryPairs = query.split("&");

      for (var queryPair : queryPairs) {
        var keyValuePair = queryPair.split("=");

        queryParameters.put(keyValuePair[0], keyValuePair[1]);
      }
    }

    return queryParameters;
  }

  private JsonElement parseBody(HttpExchange exchange) {
    StringBuilder jsonStringBody = new StringBuilder();

    try (Scanner scanner = new Scanner(exchange.getRequestBody())) {
      while (scanner.hasNext()) {
        jsonStringBody.append(scanner.nextLine());
      }
    }

    return jsonStringBody.isEmpty() ? new JsonElement(new JsonObject()) : new SolaJson().parse(jsonStringBody.toString());
  }

  private record Route(String method, String path, RouteHandler routeHandler) {
  }
}
