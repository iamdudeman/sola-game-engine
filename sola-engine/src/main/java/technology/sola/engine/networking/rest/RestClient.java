package technology.sola.engine.networking.rest;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.function.Consumer;

/**
 * RestClient defines the api for creating rest client implementations for sending HTTP requests.
 */
@NullMarked
public interface RestClient {
  /**
   * Creates and sends an HTTP request. The {@link HttpResponse} from the server as provided in a callback.
   *
   * <p>
   * Note: body is ignored when method is "GET"
   *
   * @param method               the http method for the request
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier);

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a GET request.
   *
   * @param path                 the path for the request
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void get(String path, Consumer<HttpResponse> httpResponseSupplier) {
    request("GET", path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a POST request.
   *
   * @param path                 the path for the request
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void post(String path, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a POST request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void post(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, body, httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a POST request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void post(String path, JsonObject body, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, new JsonElement(body), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a POST request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void post(String path, JsonArray body, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, new JsonElement(body), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a PUT request.
   *
   * @param path                 the path for the request
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void put(String path, Consumer<HttpResponse> httpResponseSupplier) {
    put(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a PUT request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void put(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("PUT", path, body, httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a PUT request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void put(String path, JsonObject body, Consumer<HttpResponse> httpResponseSupplier) {
    request("PUT", path, new JsonElement(body), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a PUT request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void put(String path, JsonArray body, Consumer<HttpResponse> httpResponseSupplier) {
    request("PUT", path, new JsonElement(body), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a DELETE request.
   *
   * @param path                 the path for the request
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void delete(String path, Consumer<HttpResponse> httpResponseSupplier) {
    delete(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a DELETE request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void delete(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("DELETE", path, body, httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a DELETE request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void delete(String path, JsonObject body, Consumer<HttpResponse> httpResponseSupplier) {
    request("DELETE", path, new JsonElement(body), httpResponseSupplier);
  }

  /**
   * Convenience method around {@link RestClient#request(String, String, JsonElement, Consumer)} for making a DELETE request.
   *
   * @param path                 the path for the request
   * @param body                 the request body
   * @param httpResponseSupplier the callback when the request finishes and the response is available
   */
  default void delete(String path, JsonArray body, Consumer<HttpResponse> httpResponseSupplier) {
    request("DELETE", path, new JsonElement(body), httpResponseSupplier);
  }
}
