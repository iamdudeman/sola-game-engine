package technology.sola.engine.server.rest;

/**
 * RouteHandler defines the contract for handling HTTP requests for a {@link technology.sola.engine.server.SolaServer}.
 */
@FunctionalInterface
public interface RouteHandler {
  /**
   * Method called when handling an HTTP request to produce an HTTP response.
   *
   * @param solaRequest the {@link SolaRequest}
   * @return the HTTP response data
   */
  SolaResponse handleRoute(SolaRequest solaRequest);
}
