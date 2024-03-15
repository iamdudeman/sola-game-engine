package technology.sola.engine.server.rest;

/**
 * RouteHandler defines the contract for handling HTTP requests for a {@link technology.sola.engine.server.SolaServer}.
 */
@FunctionalInterface
public interface RouteHandler {
  /**
   * Method called when handling an HTTP request. {@link RequestParameters} are given to the handler and is expected to
   * produce a {@link SolaResponse}.
   *
   * @param requestParameters the parameters from the request
   * @return the HTTP response data
   */
  SolaResponse handleRoute(RequestParameters requestParameters);
}
