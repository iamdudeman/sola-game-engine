package technology.sola.engine.server.rest;

import com.sun.net.httpserver.HttpExchange;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.util.*;

/**
 * SolaRouter handles {@link HttpExchange}s to the server via routes it has registered. If no route matches the method
 * and path from the exchange then a 404 {@link SolaResponse} will be returned.
 */
public class SolaRouter {
  private final List<Route> routes = new ArrayList<>();

  /**
   * Registers a route for desired method and path. If a path fragment is wrapped in curly braces it will be treated as
   * a path parameter (ie /{myParamName})
   *
   * @param method       the HTTP method for the route
   * @param path         the path to match for this route
   * @param routeHandler the {@link RouteHandler} called when matched
   * @return this
   */
  public SolaRouter route(String method, String path, RouteHandler routeHandler) {
    routes.add(new Route(method, path.split("/"), routeHandler));

    return this;
  }

  /**
   * Convenience method for {@link SolaRouter#route(String, String, RouteHandler)} with "GET" as the method.
   * If a path fragment is wrapped in curly braces it will be treated as a path parameter (ie /{myParamName})
   *
   * @param path         the path to match for this route
   * @param routeHandler the {@link RouteHandler} called when matched
   * @return this
   */
  public SolaRouter get(String path, RouteHandler routeHandler) {
    return route("GET", path, routeHandler);
  }

  /**
   * Convenience method for {@link SolaRouter#route(String, String, RouteHandler)} with "POST" as the method.
   * If a path fragment is wrapped in curly braces it will be treated as a path parameter (ie /{myParamName})
   *
   * @param path         the path to match for this route
   * @param routeHandler the {@link RouteHandler} called when matched
   * @return this
   */
  public SolaRouter post(String path, RouteHandler routeHandler) {
    return route("POST", path, routeHandler);
  }

  /**
   * Convenience method for {@link SolaRouter#route(String, String, RouteHandler)} with "PUT" as the method.
   * If a path fragment is wrapped in curly braces it will be treated as a path parameter (ie /{myParamName})
   *
   * @param path         the path to match for this route
   * @param routeHandler the {@link RouteHandler} called when matched
   * @return this
   */
  public SolaRouter put(String path, RouteHandler routeHandler) {
    return route("PUT", path, routeHandler);
  }

  /**
   * Convenience method for {@link SolaRouter#route(String, String, RouteHandler)} with "DELETE" as the method.
   * If a path fragment is wrapped in curly braces it will be treated as a path parameter (ie /{myParamName})
   *
   * @param path         the path to match for this route
   * @param routeHandler the {@link RouteHandler} called when matched
   * @return this
   */
  public SolaRouter delete(String path, RouteHandler routeHandler) {
    return route("DELETE", path, routeHandler);
  }

  /**
   * Method that handles an {@link HttpExchange} using routes that have been registered.
   *
   * @param httpExchange the {@code HttpExchange} to handle
   * @return the {@link SolaResponse} after the exchange was handled
   */
  public SolaResponse handleHttpExchange(HttpExchange httpExchange) {
    return routes.stream()
      .filter(route -> isRouteMatch(route, httpExchange))
      .findFirst()
      .map(route -> route.routeHandler.handleRoute(buildRequestParameters(httpExchange, route)))
      .orElse(new SolaResponse(404, new JsonObject()));
  }

  private boolean isRouteMatch(Route route, HttpExchange exchange) {
    String method = exchange.getRequestMethod();

    if (!route.method.equalsIgnoreCase(method)) {
      return false;
    }

    String path = exchange.getRequestURI().getPath();
    String[] pathParts = path.split("/");

    if (pathParts.length != route.pathParts.length) {
      return false;
    }

    for (int i = 0; i < pathParts.length; i++) {
      String routePartToMatch = route.pathParts[i];

      // path param so anything matches
      if (routePartToMatch.startsWith("{") && routePartToMatch.endsWith("}")) {
        continue;
      }

      if (!pathParts[i].equals(routePartToMatch)) {
        return false;
      }
    }

    return true;
  }

  private SolaRequest buildRequestParameters(HttpExchange exchange, Route route) {
    return new SolaRequest(
      parsePathParams(exchange, route),
      parseQueryParams(exchange),
      parseBody(exchange)
    );
  }

  private Map<String, String> parsePathParams(HttpExchange exchange, Route route) {
    String path = exchange.getRequestURI().getPath();
    Map<String, String> pathParameters = new HashMap<>();

    String[] pathParts = path.split("/");

    for (int i = 0; i < pathParts.length; i++) {
      String routePartToMatch = route.pathParts[i];

      if (routePartToMatch.startsWith("{") && routePartToMatch.endsWith("}")) {
        pathParameters.put(routePartToMatch.replace("{", "").replace("}", ""), pathParts[i]);
      }
    }

    return pathParameters;
  }

  private Map<String, String> parseQueryParams(HttpExchange exchange) {
    String query = exchange.getRequestURI().getQuery();
    Map<String, String> queryParameters = new HashMap<>();

    if (query != null) {
      var queryPairs = query.split("&");

      for (var queryPair : queryPairs) {
        var keyValuePair = queryPair.split("=");

        queryParameters.put(keyValuePair[0], keyValuePair.length == 2 ? keyValuePair[1] : null);
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

  private record Route(String method, String[] pathParts, RouteHandler routeHandler) {
  }
}
